package com.traq.core.location.controller

import android.location.Location
import com.traq.core.data.model.TrackPoint
import com.traq.core.location.model.TrackingReadiness
import com.traq.core.location.model.TrackingState
import kotlinx.coroutines.flow.StateFlow

interface TrackingController {
    val trackingState: StateFlow<TrackingState>
    val currentLocation: StateFlow<TrackPoint?>
    fun hasRequiredPermissions(): Boolean
    fun getTrackingReadiness(): TrackingReadiness
    fun startTrip(): String
    fun pauseTrip()
    fun resumeTrip()
    fun stopTrip()
    suspend fun getLastLocation(): Location?
}
