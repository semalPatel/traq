package com.traq.core.data.model

data class TripMetrics(
    val totalDistanceMeters: Double,
    val totalDurationMs: Long,
    val movingDurationMs: Long,
    val avgSpeedMps: Double,
    val maxSpeedMps: Double,
    val totalAscentMeters: Double,
    val totalDescentMeters: Double,
    val batteryUsedPercent: Int?,
    val pointCount: Int,
    val batteryEndPercent: Int? = null
)
