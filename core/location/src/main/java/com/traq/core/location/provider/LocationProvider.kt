package com.traq.core.location.provider

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.traq.core.common.model.TrackingAccuracy
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class LocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _locations = MutableSharedFlow<Location>(extraBufferCapacity = 64)
    val locations: SharedFlow<Location> = _locations.asSharedFlow()

    private var callback: LocationCallback? = null
    private var currentIntervalMs: Long = 3000L
    private var currentPriority: Int = Priority.PRIORITY_HIGH_ACCURACY
    private var currentMinDistance: Float = 2f

    fun start(accuracy: TrackingAccuracy) {
        val (priority, intervalMs, minDistance) = when (accuracy) {
            TrackingAccuracy.HIGH -> Triple(Priority.PRIORITY_HIGH_ACCURACY, 2000L, 2f)
            TrackingAccuracy.BALANCED -> Triple(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000L, 5f)
            TrackingAccuracy.LOW -> Triple(Priority.PRIORITY_LOW_POWER, 10000L, 10f)
        }
        start(intervalMs, priority, minDistance)
    }

    @SuppressLint("MissingPermission")
    fun start(
        intervalMs: Long = 3000L,
        priority: Int = Priority.PRIORITY_HIGH_ACCURACY,
        minDistance: Float = 2f
    ) {
        currentIntervalMs = intervalMs
        currentPriority = priority
        currentMinDistance = minDistance

        val request = LocationRequest.Builder(priority, intervalMs)
            .setMinUpdateDistanceMeters(minDistance)
            .setMaxUpdateDelayMillis(intervalMs * 2)
            .setWaitForAccurateLocation(false)
            .build()

        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { _locations.tryEmit(it) }
            }
        }
        fusedClient.requestLocationUpdates(request, callback!!, Looper.getMainLooper())
    }

    @SuppressLint("MissingPermission")
    fun updateInterval(intervalMs: Long) {
        if (intervalMs == currentIntervalMs) return
        stop()
        start(intervalMs, currentPriority, currentMinDistance)
    }

    fun stop() {
        callback?.let { fusedClient.removeLocationUpdates(it) }
        callback = null
    }
}
