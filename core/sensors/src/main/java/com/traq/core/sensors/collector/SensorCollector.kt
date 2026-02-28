package com.traq.core.sensors.collector

import com.traq.core.sensors.model.SensorReading
import kotlinx.coroutines.flow.StateFlow

interface SensorCollector {
    val sensorData: StateFlow<SensorReading?>
    val isMoving: StateFlow<Boolean>
    fun start()
    fun stop()
    fun getRecentReadings(windowMs: Long): List<SensorReading>
}
