package com.traq.core.export.model

import kotlinx.serialization.Serializable

@Serializable
data class TraqFile(
    val version: String = "1.0",
    val generator: String = "Traq",
    val exportedAt: String,
    val trip: TraqTrip
)

@Serializable
data class TraqTrip(
    val id: String,
    val name: String? = null,
    val startTime: String,
    val endTime: String? = null,
    val metrics: TraqMetrics,
    val trackPoints: List<TraqPoint>,
    val aiMetadata: TraqAiMetadata? = null
)

@Serializable
data class TraqMetrics(
    val totalDistanceMeters: Double,
    val movingTimeMs: Long,
    val stoppedTimeMs: Long,
    val avgSpeedMps: Double,
    val maxSpeedMps: Double,
    val totalAscentMeters: Double,
    val totalDescentMeters: Double,
    val batteryUsedPercent: Int? = null,
    val pointCount: Int,
    val dominantMode: String? = null
)

@Serializable
data class TraqPoint(
    val t: Long,
    val lat: Double,
    val lon: Double,
    val alt: Double? = null,
    val spd: Float? = null,
    val brg: Float? = null,
    val acc: Float? = null,
    val vacc: Float? = null,
    val mode: String? = null,
    val interp: Boolean = false
)

@Serializable
data class TraqAiMetadata(
    val samplingStrategy: String? = null,
    val modelVersions: Map<String, String>? = null,
    val interpolatedSegments: List<TraqInterpolatedSegment>? = null
)

@Serializable
data class TraqInterpolatedSegment(
    val startIdx: Int,
    val endIdx: Int,
    val reason: String,
    val method: String
)
