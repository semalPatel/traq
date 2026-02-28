package com.traq.core.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "track_points",
    indices = [
        Index(value = ["tripId", "timestamp"]),
        Index(value = ["tripId", "segmentIndex"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TrackPointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: String,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double?,
    val speed: Float?,
    val bearing: Float?,
    val horizontalAccuracy: Float?,
    val verticalAccuracy: Float?,
    val provider: String,
    val transportMode: String?,
    val isInterpolated: Boolean,
    val batteryPercent: Int?,
    val accelerometerMagnitude: Float?,
    val segmentIndex: Int
)
