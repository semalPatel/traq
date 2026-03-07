package com.traq.core.location.controller

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.traq.core.common.model.TripStatus
import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.Trip
import com.traq.core.data.model.TripMetrics
import com.traq.core.data.repository.TripRepository
import com.traq.core.location.model.TrackingReadiness
import com.traq.core.location.model.TrackingState
import com.traq.core.location.provider.LocationProvider
import com.traq.core.location.service.TrackingService
import com.traq.core.permissions.PermissionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackingControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tripRepository: TripRepository,
    private val locationProvider: LocationProvider,
    private val permissionManager: PermissionManager
) : TrackingController {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var service: TrackingService? = null
    private var bound = false

    private val _trackingState = MutableStateFlow(TrackingState.IDLE)
    override val trackingState: StateFlow<TrackingState> = _trackingState.asStateFlow()

    private val _currentLocation = MutableStateFlow<TrackPoint?>(null)
    override val currentLocation: StateFlow<TrackPoint?> = _currentLocation.asStateFlow()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as TrackingService.LocalBinder
            service = localBinder.getService()
            bound = true
            scope.launch {
                service?.trackingState?.collect { _trackingState.value = it }
            }
            scope.launch {
                service?.currentLocation?.collect { _currentLocation.value = it }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
            bound = false
        }
    }

    override fun hasRequiredPermissions(): Boolean {
        return getTrackingReadiness().canStartTrip
    }

    override fun getTrackingReadiness(): TrackingReadiness {
        return TrackingReadiness(
            hasForegroundLocation = permissionManager.hasLocationPermission(),
            hasBackgroundLocation = permissionManager.hasBackgroundLocationPermission(),
            hasNotifications = permissionManager.hasNotificationPermission(),
            isBatteryOptimizationDisabled = permissionManager.isBatteryOptimizationDisabled()
        )
    }

    override fun startTrip(): String {
        val readiness = getTrackingReadiness()
        if (!readiness.canStartTrip) {
            val missing = readiness.blockingRequirements.joinToString { requirement -> requirement.displayName }
            throw SecurityException("Tracking is not ready. Missing: $missing.")
        }

        val tripId = UUID.randomUUID().toString()
        scope.launch {
            tripRepository.createTrip(
                Trip(
                    id = tripId,
                    name = null,
                    startTime = Instant.now(),
                    endTime = null,
                    status = TripStatus.ACTIVE,
                    dominantMode = null,
                    metrics = TripMetrics(0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, null, 0),
                    startLatitude = 0.0,
                    startLongitude = 0.0,
                    endLatitude = null,
                    endLongitude = null
                )
            )
        }

        val intent = Intent(context, TrackingService::class.java).apply {
            action = TrackingService.ACTION_START
            putExtra(TrackingService.EXTRA_TRIP_ID, tripId)
        }
        ContextCompat.startForegroundService(context, intent)
        context.bindService(Intent(context, TrackingService::class.java), connection, Context.BIND_AUTO_CREATE)
        return tripId
    }

    override fun pauseTrip() {
        val intent = Intent(context, TrackingService::class.java).apply {
            action = TrackingService.ACTION_PAUSE
        }
        context.startService(intent)
    }

    override fun resumeTrip() {
        val intent = Intent(context, TrackingService::class.java).apply {
            action = TrackingService.ACTION_RESUME
        }
        context.startService(intent)
    }

    override fun stopTrip() {
        val intent = Intent(context, TrackingService::class.java).apply {
            action = TrackingService.ACTION_STOP
        }
        context.startService(intent)
        if (bound) {
            try { context.unbindService(connection) } catch (_: Exception) {}
            bound = false
        }
    }

    override suspend fun getLastLocation(): Location? {
        return locationProvider.getLastLocation()
    }
}
