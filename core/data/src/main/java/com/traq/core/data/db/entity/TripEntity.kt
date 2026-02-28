package com.traq.core.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val startTime: Long,
    val endTime: Long?,
    val status: String,
    val dominantMode: String?,
    val totalDistanceMeters: Double,
    val totalDurationMs: Long,
    val movingDurationMs: Long,
    val avgSpeedMps: Double,
    val maxSpeedMps: Double,
    val totalAscentMeters: Double,
    val totalDescentMeters: Double,
    val startLatitude: Double,
    val startLongitude: Double,
    val endLatitude: Double?,
    val endLongitude: Double?,
    val batteryStartPercent: Int,
    val batteryEndPercent: Int?,
    val pointCount: Int
)
