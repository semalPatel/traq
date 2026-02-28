package com.traq.core.sensors.model

data class SensorReading(
    val timestamp: Long,
    val accelerometer: FloatArray,
    val gyroscope: FloatArray?,
    val magnetometer: FloatArray?,
    val stepCount: Int?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SensorReading) return false
        return timestamp == other.timestamp
    }

    override fun hashCode(): Int = timestamp.hashCode()
}
