package com.traq.core.data.model

import com.traq.core.common.model.SegmentReason
import com.traq.core.common.model.TransportMode
import java.time.Instant

data class TripSegment(
    val id: Long = 0,
    val tripId: String,
    val segmentIndex: Int,
    val startTime: Instant,
    val endTime: Instant?,
    val transportMode: TransportMode?,
    val distanceMeters: Double,
    val reason: SegmentReason
)
