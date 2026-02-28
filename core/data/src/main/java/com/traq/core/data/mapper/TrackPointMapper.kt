package com.traq.core.data.mapper

import com.traq.core.common.model.TransportMode
import com.traq.core.data.db.entity.TrackPointEntity
import com.traq.core.data.model.TrackPoint
import java.time.Instant

object TrackPointMapper {
    fun TrackPointEntity.toDomain(): TrackPoint = TrackPoint(
        id = id,
        tripId = tripId,
        timestamp = Instant.ofEpochMilli(timestamp),
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        speed = speed,
        bearing = bearing,
        horizontalAccuracy = horizontalAccuracy,
        verticalAccuracy = verticalAccuracy,
        transportMode = transportMode?.let { TransportMode.valueOf(it) },
        isInterpolated = isInterpolated,
        segmentIndex = segmentIndex
    )

    fun TrackPoint.toEntity(): TrackPointEntity = TrackPointEntity(
        id = id,
        tripId = tripId,
        timestamp = timestamp.toEpochMilli(),
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        speed = speed,
        bearing = bearing,
        horizontalAccuracy = horizontalAccuracy,
        verticalAccuracy = verticalAccuracy,
        provider = "FUSED",
        transportMode = transportMode?.name,
        isInterpolated = isInterpolated,
        batteryPercent = null,
        accelerometerMagnitude = null,
        segmentIndex = segmentIndex
    )
}
