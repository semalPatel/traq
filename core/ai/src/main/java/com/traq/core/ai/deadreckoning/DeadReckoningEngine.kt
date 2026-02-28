package com.traq.core.ai.deadreckoning

import com.traq.core.data.model.TrackPoint
import com.traq.core.sensors.model.SensorReading

interface DeadReckoningEngine {
    fun estimatePosition(
        lastKnown: TrackPoint,
        sensorReadings: List<SensorReading>,
        elapsedMs: Long
    ): TrackPoint
}
