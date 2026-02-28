package com.traq.core.data.model

import com.traq.core.common.model.TransportMode
import java.time.Instant

data class TrackPoint(
    val id: Long = 0,
    val tripId: String,
    val timestamp: Instant,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double?,
    val speed: Float?,
    val bearing: Float?,
    val horizontalAccuracy: Float?,
    val verticalAccuracy: Float?,
    val transportMode: TransportMode?,
    val isInterpolated: Boolean,
    val segmentIndex: Int
)
