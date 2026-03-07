package com.traq.core.data.model

import com.traq.core.common.model.TransportMode
import com.traq.core.common.model.TripStatus
import java.time.Instant

data class Trip(
    val id: String,
    val name: String?,
    val startTime: Instant,
    val endTime: Instant?,
    val status: TripStatus,
    val dominantMode: TransportMode?,
    val metrics: TripMetrics,
    val startLatitude: Double,
    val startLongitude: Double,
    val endLatitude: Double?,
    val endLongitude: Double?,
    val batteryStartPercent: Int = 100,
    val batteryEndPercent: Int? = null
)
