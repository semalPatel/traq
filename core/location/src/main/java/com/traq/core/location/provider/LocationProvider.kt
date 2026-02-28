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

    @SuppressLint("MissingPermission")
    fun start(intervalMs: Long = 3000L, priority: Int = Priority.PRIORITY_HIGH_ACCURACY) {
        currentIntervalMs = intervalMs
        val request = LocationRequest.Builder(priority, intervalMs)
            .setMinUpdateDistanceMeters(2f)
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
        start(intervalMs)
    }

    fun stop() {
        callback?.let { fusedClient.removeLocationUpdates(it) }
        callback = null
    }
}
