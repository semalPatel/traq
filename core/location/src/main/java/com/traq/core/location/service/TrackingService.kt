package com.traq.core.location.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ServiceCompat
import com.traq.core.ai.classification.TransportClassifier
import com.traq.core.ai.deadreckoning.DeadReckoningEngine
import com.traq.core.ai.filter.GpsProcessor
import com.traq.core.ai.lifecycle.TripLifecycleManager
import com.traq.core.ai.sampling.AdaptiveSampler
import com.traq.core.ai.util.GeoMath.haversineDistance
import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.TripMetrics
import com.traq.core.data.repository.TrackPointRepository
import com.traq.core.data.repository.TripRepository
import com.traq.core.data.repository.UserPreferencesRepository
import com.traq.core.location.model.TrackingState
import com.traq.core.location.provider.LocationProvider
import com.traq.core.location.util.BatteryMonitor
import com.traq.core.location.util.WakeLockManager
import com.traq.core.sensors.collector.SensorCollector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : Service() {

    @Inject lateinit var locationProvider: LocationProvider
    @Inject lateinit var trackPointRepository: TrackPointRepository
    @Inject lateinit var tripRepository: TripRepository
    @Inject lateinit var notificationManager: TrackingNotificationManager
    @Inject lateinit var wakeLockManager: WakeLockManager
    @Inject lateinit var batteryMonitor: BatteryMonitor
    @Inject lateinit var sensorCollector: SensorCollector
    @Inject lateinit var gpsProcessor: GpsProcessor
    @Inject lateinit var adaptiveSampler: AdaptiveSampler
    @Inject lateinit var transportClassifier: TransportClassifier
    @Inject lateinit var deadReckoningEngine: DeadReckoningEngine
    @Inject lateinit var tripLifecycleManager: TripLifecycleManager
    @Inject lateinit var prefsRepository: UserPreferencesRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var locationCollectionJob: Job? = null
    private var timerJob: Job? = null

    private val binder = LocalBinder()
    private var currentTripId: String? = null
    private var startTimeMs: Long = 0L
    private var pauseStartMs: Long = 0L
    private var totalPausedMs: Long = 0L
    private var lastRecordedPoint: TrackPoint? = null
    private var totalDistanceMeters: Double = 0.0
    private var totalAscentMeters: Double = 0.0
    private var totalDescentMeters: Double = 0.0
    private var maxSpeedMps: Float = 0f
    private var pointCount: Int = 0
    private var currentSamplingIntervalMs: Long = 3000L
    private var lastRawLocationAtMs: Long = 0L
    private var lastEstimatedLocationAtMs: Long = 0L
    private var batteryStartPercent: Int = 0

    private val _trackingState = MutableStateFlow(TrackingState.IDLE)
    val trackingState: StateFlow<TrackingState> = _trackingState.asStateFlow()

    private val _currentLocation = MutableStateFlow<TrackPoint?>(null)
    val currentLocation: StateFlow<TrackPoint?> = _currentLocation.asStateFlow()

    inner class LocalBinder : Binder() {
        fun getService(): TrackingService = this@TrackingService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTracking(intent.getStringExtra(EXTRA_TRIP_ID))
            ACTION_PAUSE -> pauseTracking()
            ACTION_RESUME -> resumeTracking()
            ACTION_STOP -> stopTracking()
            null -> {
                if (currentTripId == null) {
                    serviceScope.launch {
                        val activeTrip = tripRepository.getActiveTrip()
                        if (activeTrip != null) {
                            Log.i(TAG, "Recovering active trip ${activeTrip.id}")
                            recoverTrip(activeTrip.id)
                        } else {
                            stopSelf()
                        }
                    }
                }
            }
        }
        return START_STICKY
    }

    private fun startTracking(tripId: String?) {
        currentTripId = tripId ?: UUID.randomUUID().toString()
        startTimeMs = System.currentTimeMillis()
        batteryStartPercent = batteryMonitor.getBatteryPercent()
        totalPausedMs = 0L
        totalDistanceMeters = 0.0
        totalAscentMeters = 0.0
        totalDescentMeters = 0.0
        maxSpeedMps = 0f
        pointCount = 0
        currentSamplingIntervalMs = 3000L
        lastRecordedPoint = null
        lastRawLocationAtMs = 0L
        lastEstimatedLocationAtMs = 0L

        notificationManager.createNotificationChannel()
        val notification = notificationManager.buildNotification(TrackingState.IDLE)

        if (Build.VERSION.SDK_INT >= 34) {
            ServiceCompat.startForeground(
                this, TrackingNotificationManager.NOTIFICATION_ID, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(TrackingNotificationManager.NOTIFICATION_ID, notification)
        }

        wakeLockManager.acquire()

        serviceScope.launch {
            val accuracy = prefsRepository.trackingAccuracy.first()
            locationProvider.start(accuracy)
            sensorCollector.start()

            locationCollectionJob = launch {
                locationProvider.locations.collect { location ->
                    processLocation(location)
                }
            }
        }

        timerJob = serviceScope.launch {
            while (true) {
                delay(1000)
                maybeEstimateLocation()
                updateState()
            }
        }

        updateState()
    }

    private fun recoverTrip(tripId: String) {
        currentTripId = tripId

        notificationManager.createNotificationChannel()
        val notification = notificationManager.buildNotification(TrackingState.IDLE)

        if (Build.VERSION.SDK_INT >= 34) {
            ServiceCompat.startForeground(
                this, TrackingNotificationManager.NOTIFICATION_ID, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(TrackingNotificationManager.NOTIFICATION_ID, notification)
        }

        wakeLockManager.acquire()

        serviceScope.launch {
            restorePersistedState(tripId)
            val accuracy = prefsRepository.trackingAccuracy.first()
            locationProvider.start(accuracy)
            sensorCollector.start()

            locationCollectionJob = launch {
                locationProvider.locations.collect { location ->
                    processLocation(location)
                }
            }
        }

        timerJob = serviceScope.launch {
            while (true) {
                delay(1000)
                maybeEstimateLocation()
                updateState()
            }
        }

        updateState()
    }

    private suspend fun restorePersistedState(tripId: String) {
        val trip = tripRepository.getTripFlow(tripId).first() ?: return
        val points = trackPointRepository.getTrackPointsFlow(tripId).first()

        startTimeMs = trip.startTime.toEpochMilli()
        batteryStartPercent = trip.batteryStartPercent
        totalPausedMs = 0L
        totalDistanceMeters = 0.0
        totalAscentMeters = 0.0
        totalDescentMeters = 0.0
        maxSpeedMps = 0f
        pointCount = 0
        lastRecordedPoint = null

        points.forEach { point ->
            lastRecordedPoint?.let { prev ->
                totalDistanceMeters += haversineDistance(
                    prev.latitude,
                    prev.longitude,
                    point.latitude,
                    point.longitude
                )
                val previousAltitude = prev.altitude
                val currentAltitude = point.altitude
                if (previousAltitude != null && currentAltitude != null) {
                    val elevDiff = currentAltitude - previousAltitude
                    if (elevDiff > 0) totalAscentMeters += elevDiff
                    else totalDescentMeters += kotlin.math.abs(elevDiff)
                }
            }
            point.speed?.let { if (it > maxSpeedMps) maxSpeedMps = it }
            pointCount++
            lastRecordedPoint = point
        }

        _currentLocation.value = lastRecordedPoint
        lastRawLocationAtMs = points.lastOrNull { !it.isInterpolated }?.timestamp?.toEpochMilli()
            ?: trip.startTime.toEpochMilli()
        lastEstimatedLocationAtMs = lastRecordedPoint
            ?.takeIf { it.isInterpolated }
            ?.timestamp
            ?.toEpochMilli()
            ?: 0L
        Log.i(TAG, "Recovered trip $tripId with $pointCount recorded points")
    }

    private fun pauseTracking() {
        pauseStartMs = System.currentTimeMillis()
        locationProvider.stop()
        sensorCollector.stop()
        locationCollectionJob?.cancel()
        updateState(isPaused = true)
    }

    private fun resumeTracking() {
        totalPausedMs += System.currentTimeMillis() - pauseStartMs

        serviceScope.launch {
            val accuracy = prefsRepository.trackingAccuracy.first()
            locationProvider.start(accuracy)
            sensorCollector.start()

            locationCollectionJob = launch {
                locationProvider.locations.collect { location ->
                    processLocation(location)
                }
            }
        }

        updateState(isPaused = false)
    }

    private fun stopTracking() {
        locationProvider.stop()
        sensorCollector.stop()
        gpsProcessor.reset()
        tripLifecycleManager.reset()
        locationCollectionJob?.cancel()
        timerJob?.cancel()
        wakeLockManager.release()

        val tripId = currentTripId
        val elapsed = System.currentTimeMillis() - startTimeMs - totalPausedMs

        serviceScope.launch(NonCancellable) {
            tripId?.let { id ->
                val batteryEndPercent = batteryMonitor.getBatteryPercent()
                val batteryUsedPercent = (batteryStartPercent - batteryEndPercent).coerceAtLeast(0)

                tripRepository.completeTrip(
                    id, Instant.now(),
                    TripMetrics(
                        totalDistanceMeters = totalDistanceMeters,
                        totalDurationMs = elapsed,
                        movingDurationMs = elapsed,
                        avgSpeedMps = if (elapsed > 0) (totalDistanceMeters / (elapsed / 1000.0)) else 0.0,
                        maxSpeedMps = maxSpeedMps.toDouble(),
                        totalAscentMeters = totalAscentMeters,
                        totalDescentMeters = totalDescentMeters,
                        batteryUsedPercent = batteryUsedPercent,
                        pointCount = pointCount,
                        batteryEndPercent = batteryEndPercent
                    )
                )
            }
            _trackingState.value = TrackingState.IDLE
            currentTripId = null
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }

    private suspend fun processLocation(location: Location) {
        val tripId = currentTripId ?: return

        val rawPoint = TrackPoint(
            tripId = tripId,
            timestamp = Instant.ofEpochMilli(location.time),
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = if (location.hasAltitude()) location.altitude else null,
            speed = if (location.hasSpeed()) location.speed else null,
            bearing = if (location.hasBearing()) location.bearing else null,
            horizontalAccuracy = if (location.hasAccuracy()) location.accuracy else null,
            verticalAccuracy = if (Build.VERSION.SDK_INT >= 26 && location.hasVerticalAccuracy()) location.verticalAccuracyMeters else null,
            transportMode = null,
            isInterpolated = false,
            segmentIndex = 0
        )

        val smoothed = gpsProcessor.smooth(rawPoint)

        if (gpsProcessor.isAnomaly(smoothed, lastRecordedPoint)) return

        val sensorWindow = sensorCollector.getRecentReadings(10_000)
        val mode = transportClassifier.classify(sensorWindow, smoothed.speed)
        val withMode = smoothed.copy(transportMode = mode)
        recordPoint(withMode)
        lastRawLocationAtMs = location.time

        val newInterval = adaptiveSampler.getRecommendedIntervalMs(
            currentSpeed = withMode.speed,
            bearingChangeRate = null,
            accuracy = withMode.horizontalAccuracy,
            batteryPercent = batteryMonitor.getBatteryPercent(),
            isMoving = sensorCollector.isMoving.value
        )
        locationProvider.updateInterval(newInterval)
        currentSamplingIntervalMs = newInterval
    }

    private suspend fun maybeEstimateLocation() {
        if (currentTripId == null || _trackingState.value.isPaused) return

        val lastPoint = lastRecordedPoint ?: return
        val now = System.currentTimeMillis()
        val lastFixAge = now - lastRawLocationAtMs
        val timeSinceLastEstimate = now - lastEstimatedLocationAtMs

        if (lastRawLocationAtMs == 0L) return
        if (lastFixAge < STALE_LOCATION_THRESHOLD_MS) return
        if (lastFixAge > MAX_ESTIMATION_WINDOW_MS) return
        if (!sensorCollector.isMoving.value) return
        if ((lastPoint.speed ?: 0f) < MIN_ESTIMATION_SPEED_MPS) return
        if (timeSinceLastEstimate < currentSamplingIntervalMs) return

        val estimated = deadReckoningEngine.estimatePosition(
            lastKnown = lastPoint,
            sensorReadings = sensorCollector.getRecentReadings(10_000),
            elapsedMs = currentSamplingIntervalMs
        )

        if (gpsProcessor.isAnomaly(estimated, lastRecordedPoint)) return

        recordPoint(estimated)
        lastEstimatedLocationAtMs = now
        Log.w(TAG, "Using estimated position after ${lastFixAge}ms without a fresh GPS update")
    }

    private suspend fun recordPoint(point: TrackPoint) {
        lastRecordedPoint?.let { prev ->
            totalDistanceMeters += haversineDistance(
                prev.latitude, prev.longitude, point.latitude, point.longitude
            )
            val previousAltitude = prev.altitude
            val currentAltitude = point.altitude
            if (previousAltitude != null && currentAltitude != null) {
                val elevDiff = currentAltitude - previousAltitude
                if (elevDiff > 0) totalAscentMeters += elevDiff
                else totalDescentMeters += kotlin.math.abs(elevDiff)
            }
        }
        point.speed?.let { if (it > maxSpeedMps) maxSpeedMps = it }

        trackPointRepository.insert(point)
        pointCount++
        lastRecordedPoint = point
        _currentLocation.value = point
    }

    private fun updateState(isPaused: Boolean = _trackingState.value.isPaused) {
        val now = System.currentTimeMillis()
        val elapsed = if (isPaused) {
            pauseStartMs - startTimeMs - totalPausedMs
        } else {
            now - startTimeMs - totalPausedMs
        }
        val locationAgeMs = if (lastRawLocationAtMs > 0L) now - lastRawLocationAtMs else null
        val isLocationStale = !isPaused && (locationAgeMs ?: 0L) >= STALE_LOCATION_THRESHOLD_MS

        val state = TrackingState(
            isTracking = true,
            isPaused = isPaused,
            tripId = currentTripId,
            elapsedMs = elapsed.coerceAtLeast(0),
            distanceMeters = totalDistanceMeters,
            currentSpeedMps = _currentLocation.value?.speed,
            pointsRecorded = pointCount,
            batteryPercent = batteryMonitor.getBatteryPercent(),
            currentMode = _currentLocation.value?.transportMode,
            samplingIntervalMs = currentSamplingIntervalMs,
            isLocationStale = isLocationStale,
            lastLocationAgeMs = locationAgeMs,
            isUsingEstimatedLocation = _currentLocation.value?.isInterpolated == true
        )
        _trackingState.value = state
        notificationManager.updateNotification(state)
    }

    override fun onDestroy() {
        locationProvider.stop()
        sensorCollector.stop()
        wakeLockManager.release()
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "TrackingService"
        private const val STALE_LOCATION_THRESHOLD_MS = 15_000L
        private const val MAX_ESTIMATION_WINDOW_MS = 60_000L
        private const val MIN_ESTIMATION_SPEED_MPS = 0.8f
        const val ACTION_START = "com.traq.action.START"
        const val ACTION_PAUSE = "com.traq.action.PAUSE"
        const val ACTION_RESUME = "com.traq.action.RESUME"
        const val ACTION_STOP = "com.traq.action.STOP"
        const val EXTRA_TRIP_ID = "trip_id"
    }
}
