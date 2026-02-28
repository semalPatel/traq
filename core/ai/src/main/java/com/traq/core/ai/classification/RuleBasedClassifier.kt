package com.traq.core.ai.classification

import com.traq.core.common.model.TransportMode
import com.traq.core.sensors.model.SensorReading
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class RuleBasedClassifier @Inject constructor() : TransportClassifier {

    override fun classify(sensorWindow: List<SensorReading>, currentSpeed: Float?): TransportMode {
        val speed = currentSpeed ?: return classifyFromSensors(sensorWindow)

        return when {
            speed < 0.5f -> TransportMode.STATIONARY
            speed < 2.0f -> classifySlowMovement(sensorWindow)
            speed < 6.0f -> classifyMediumSpeed(sensorWindow, speed)
            speed < 12.0f -> classifyFastMovement(sensorWindow)
            else -> TransportMode.DRIVING
        }
    }

    private fun classifyFromSensors(window: List<SensorReading>): TransportMode {
        if (window.isEmpty()) return TransportMode.STATIONARY
        val variance = computeAccelVariance(window)
        return when {
            variance < 0.3f -> TransportMode.STATIONARY
            variance < 1.5f -> TransportMode.WALKING
            variance > 2.0f -> TransportMode.DRIVING
            else -> TransportMode.STATIONARY
        }
    }

    private fun classifySlowMovement(window: List<SensorReading>): TransportMode {
        val variance = computeAccelVariance(window)
        return if (variance > 0.5f) TransportMode.WALKING else TransportMode.STATIONARY
    }

    private fun classifyMediumSpeed(window: List<SensorReading>, speed: Float): TransportMode {
        val variance = computeAccelVariance(window)
        return when {
            speed > 4f && variance > 1.0f -> TransportMode.RUNNING
            variance < 0.8f -> TransportMode.CYCLING
            else -> TransportMode.WALKING
        }
    }

    private fun classifyFastMovement(window: List<SensorReading>): TransportMode {
        val variance = computeAccelVariance(window)
        return if (variance < 1.0f) TransportMode.CYCLING else TransportMode.DRIVING
    }

    private fun computeAccelVariance(window: List<SensorReading>): Float {
        if (window.isEmpty()) return 0f
        val magnitudes = window.map { reading ->
            sqrt(reading.accelerometer[0].pow(2) + reading.accelerometer[1].pow(2) + reading.accelerometer[2].pow(2))
        }
        val mean = magnitudes.average().toFloat()
        return magnitudes.map { (it - mean).pow(2) }.average().toFloat()
    }
}
