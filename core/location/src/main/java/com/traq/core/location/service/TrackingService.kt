package com.traq.core.location.service

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.traq.core.common.model.TransportMode
import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.Trip
import com.traq.core.data.model.TripMetrics
import com.traq.core.data.repository.TrackPointRepository
import com.traq.core.data.repository.TripRepository
import com.traq.core.location.model.TrackingState
import com.traq.core.location.provider.LocationProvider
import com.traq.core.location.util.BatteryMonitor
import com.traq.core.location.util.WakeLockManager
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
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class TrackingService : Service() {

    @Inject lateinit var locationProvider: LocationProvider
    @Inject lateinit var trackPointRepository: TrackPointRepository
    @Inject lateinit var tripRepository: TripRepository
    @Inject lateinit var notificationManager: TrackingNotificationManager
    @Inject lateinit var wakeLockManager: WakeLockManager
    @Inject lateinit var batteryMonitor: BatteryMonitor

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
    private var maxSpeedMps: Float = 0f
    private var pointCount: Int = 0

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
        }
        return START_STICKY
    }

    private fun startTracking(tripId: String?) {
        currentTripId = tripId ?: UUID.randomUUID().toString()
        startTimeMs = System.currentTimeMillis()
        totalPausedMs = 0L
        totalDistanceMeters = 0.0
        maxSpeedMps = 0f
        pointCount = 0
        lastRecordedPoint = null

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
        locationProvider.start()

        locationCollectionJob = serviceScope.launch {
            locationProvider.locations.collect { location ->
                processLocation(location)
            }
        }

        timerJob = serviceScope.launch {
            while (true) {
                delay(1000)
                updateState()
            }
        }

        updateState()
    }

    private fun pauseTracking() {
        pauseStartMs = System.currentTimeMillis()
        locationProvider.stop()
        locationCollectionJob?.cancel()
        updateState(isPaused = true)
    }

    private fun resumeTracking() {
        totalPausedMs += System.currentTimeMillis() - pauseStartMs
        locationProvider.start()
        locationCollectionJob = serviceScope.launch {
            locationProvider.locations.collect { location ->
                processLocation(location)
            }
        }
        updateState(isPaused = false)
    }

    private fun stopTracking() {
        locationProvider.stop()
        locationCollectionJob?.cancel()
        timerJob?.cancel()
        wakeLockManager.release()

        serviceScope.launch {
            currentTripId?.let { id ->
                val elapsed = System.currentTimeMillis() - startTimeMs - totalPausedMs
                tripRepository.completeTrip(
                    id, Instant.now(),
                    TripMetrics(
                        totalDistanceMeters = totalDistanceMeters,
                        totalDurationMs = elapsed,
                        movingDurationMs = elapsed,
                        avgSpeedMps = if (elapsed > 0) (totalDistanceMeters / (elapsed / 1000.0)).toDouble() else 0.0,
                        maxSpeedMps = maxSpeedMps.toDouble(),
                        totalAscentMeters = 0.0,
                        totalDescentMeters = 0.0,
                        batteryUsedPercent = null,
                        pointCount = pointCount
                    )
                )
            }
        }

        _trackingState.value = TrackingState.IDLE
        currentTripId = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private suspend fun processLocation(location: Location) {
        val tripId = currentTripId ?: return
        val point = TrackPoint(
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

        lastRecordedPoint?.let { prev ->
            totalDistanceMeters += haversineDistance(
                prev.latitude, prev.longitude, point.latitude, point.longitude
            )
        }
        point.speed?.let { if (it > maxSpeedMps) maxSpeedMps = it }

        trackPointRepository.insert(point)
        pointCount++
        lastRecordedPoint = point
        _currentLocation.value = point
    }

    private fun updateState(isPaused: Boolean = _trackingState.value.isPaused) {
        val elapsed = if (isPaused) {
            pauseStartMs - startTimeMs - totalPausedMs
        } else {
            System.currentTimeMillis() - startTimeMs - totalPausedMs
        }

        val state = TrackingState(
            isTracking = true,
            isPaused = isPaused,
            tripId = currentTripId,
            elapsedMs = elapsed.coerceAtLeast(0),
            distanceMeters = totalDistanceMeters,
            currentSpeedMps = _currentLocation.value?.speed,
            pointsRecorded = pointCount,
            batteryPercent = batteryMonitor.getBatteryPercent(),
            currentMode = null,
            samplingIntervalMs = 3000
        )
        _trackingState.value = state
        notificationManager.updateNotification(state)
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        return r * 2 * asin(sqrt(a))
    }

    companion object {
        const val ACTION_START = "com.traq.action.START"
        const val ACTION_PAUSE = "com.traq.action.PAUSE"
        const val ACTION_RESUME = "com.traq.action.RESUME"
        const val ACTION_STOP = "com.traq.action.STOP"
        const val EXTRA_TRIP_ID = "trip_id"
    }
}
