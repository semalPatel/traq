package com.traq.core.data.mapper

import com.traq.core.common.model.TransportMode
import com.traq.core.common.model.TripStatus
import com.traq.core.data.db.entity.TripEntity
import com.traq.core.data.model.Trip
import com.traq.core.data.model.TripMetrics
import java.time.Instant

object TripMapper {
    fun TripEntity.toDomain(): Trip = Trip(
        id = id,
        name = name,
        startTime = Instant.ofEpochMilli(startTime),
        endTime = endTime?.let { Instant.ofEpochMilli(it) },
        status = TripStatus.valueOf(status),
        dominantMode = dominantMode?.let { TransportMode.valueOf(it) },
        metrics = TripMetrics(
            totalDistanceMeters = totalDistanceMeters,
            totalDurationMs = totalDurationMs,
            movingDurationMs = movingDurationMs,
            avgSpeedMps = avgSpeedMps,
            maxSpeedMps = maxSpeedMps,
            totalAscentMeters = totalAscentMeters,
            totalDescentMeters = totalDescentMeters,
            batteryUsedPercent = if (batteryEndPercent != null) batteryStartPercent - batteryEndPercent else null,
            pointCount = pointCount
        ),
        startLatitude = startLatitude,
        startLongitude = startLongitude,
        endLatitude = endLatitude,
        endLongitude = endLongitude
    )

    fun Trip.toEntity(): TripEntity = TripEntity(
        id = id,
        name = name,
        startTime = startTime.toEpochMilli(),
        endTime = endTime?.toEpochMilli(),
        status = status.name,
        dominantMode = dominantMode?.name,
        totalDistanceMeters = metrics.totalDistanceMeters,
        totalDurationMs = metrics.totalDurationMs,
        movingDurationMs = metrics.movingDurationMs,
        avgSpeedMps = metrics.avgSpeedMps,
        maxSpeedMps = metrics.maxSpeedMps,
        totalAscentMeters = metrics.totalAscentMeters,
        totalDescentMeters = metrics.totalDescentMeters,
        startLatitude = startLatitude,
        startLongitude = startLongitude,
        endLatitude = endLatitude,
        endLongitude = endLongitude,
        batteryStartPercent = 100,
        batteryEndPercent = metrics.batteryUsedPercent?.let { 100 - it },
        pointCount = metrics.pointCount
    )
}
