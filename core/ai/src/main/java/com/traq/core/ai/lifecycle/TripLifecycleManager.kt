package com.traq.core.ai.lifecycle

import com.traq.core.ai.util.GeoMath
import com.traq.core.sensors.collector.SensorCollector
import javax.inject.Inject

class TripLifecycleManager @Inject constructor(
    private val sensorCollector: SensorCollector
) {
    private val stationaryThresholdMs = 120_000L
    private var lastMovementTime = System.currentTimeMillis()

    fun shouldAutoPause(): Boolean {
        if (sensorCollector.isMoving.value) {
            lastMovementTime = System.currentTimeMillis()
            return false
        }
        return (System.currentTimeMillis() - lastMovementTime) > stationaryThresholdMs
    }

    fun shouldAutoResume(): Boolean = sensorCollector.isMoving.value

    fun shouldSuggestStop(
        currentLat: Double, currentLon: Double,
        startLat: Double, startLon: Double,
        stationaryDurationMs: Long
    ): Boolean {
        val nearStart = GeoMath.haversineDistance(currentLat, currentLon, startLat, startLon) < 100.0
        return nearStart && stationaryDurationMs > 300_000L
    }

    fun reset() {
        lastMovementTime = System.currentTimeMillis()
    }
}
