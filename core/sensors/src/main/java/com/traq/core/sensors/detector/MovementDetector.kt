package com.traq.core.sensors.detector

import com.traq.core.sensors.model.SensorReading
import kotlin.math.pow
import kotlin.math.sqrt

class MovementDetector {
    private val windowSize = 50
    private val stationaryThreshold = 0.5f
    private val recentMagnitudes = ArrayDeque<Float>(windowSize)

    fun isMoving(reading: SensorReading): Boolean {
        val magnitude = sqrt(
            reading.accelerometer[0].pow(2) +
            reading.accelerometer[1].pow(2) +
            reading.accelerometer[2].pow(2)
        )

        if (recentMagnitudes.size >= windowSize) recentMagnitudes.removeFirst()
        recentMagnitudes.addLast(magnitude)

        if (recentMagnitudes.size < windowSize / 2) return true

        val mean = recentMagnitudes.average().toFloat()
        val variance = recentMagnitudes.map { (it - mean).pow(2) }.average().toFloat()

        return variance > stationaryThreshold
    }

    fun reset() {
        recentMagnitudes.clear()
    }
}
