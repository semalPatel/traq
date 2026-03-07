package com.traq.core.location.model

import com.traq.core.common.model.TransportMode

data class TrackingState(
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val tripId: String? = null,
    val elapsedMs: Long = 0,
    val distanceMeters: Double = 0.0,
    val currentSpeedMps: Float? = null,
    val pointsRecorded: Int = 0,
    val batteryPercent: Int = 100,
    val currentMode: TransportMode? = null,
    val samplingIntervalMs: Long = 3000,
    val isLocationStale: Boolean = false,
    val lastLocationAgeMs: Long? = null,
    val isUsingEstimatedLocation: Boolean = false
) {
    companion object {
        val IDLE = TrackingState()
    }
}
