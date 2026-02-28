package com.traq.core.ai.deadreckoning

import com.traq.core.ai.util.GeoMath
import com.traq.core.data.model.TrackPoint
import com.traq.core.sensors.model.SensorReading
import javax.inject.Inject

class DeadReckoningEngineImpl @Inject constructor() : DeadReckoningEngine {

    override fun estimatePosition(
        lastKnown: TrackPoint,
        sensorReadings: List<SensorReading>,
        elapsedMs: Long
    ): TrackPoint {
        val speed = lastKnown.speed ?: 0f
        val bearing = lastKnown.bearing ?: 0f
        val distanceMeters = speed * (elapsedMs / 1000.0)

        val (newLat, newLon) = GeoMath.destinationPoint(
            lastKnown.latitude, lastKnown.longitude,
            distanceMeters, bearing.toDouble()
        )

        return TrackPoint(
            id = 0,
            tripId = lastKnown.tripId,
            timestamp = lastKnown.timestamp.plusMillis(elapsedMs),
            latitude = newLat,
            longitude = newLon,
            altitude = lastKnown.altitude,
            speed = speed,
            bearing = bearing,
            horizontalAccuracy = null,
            verticalAccuracy = null,
            transportMode = lastKnown.transportMode,
            isInterpolated = true,
            segmentIndex = lastKnown.segmentIndex
        )
    }
}
