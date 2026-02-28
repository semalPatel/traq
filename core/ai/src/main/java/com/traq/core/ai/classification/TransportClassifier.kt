package com.traq.core.ai.classification

import com.traq.core.common.model.TransportMode
import com.traq.core.sensors.model.SensorReading

interface TransportClassifier {
    fun classify(sensorWindow: List<SensorReading>, currentSpeed: Float?): TransportMode
}
