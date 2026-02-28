package com.traq.core.sensors.collector

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.traq.core.sensors.buffer.SensorRingBuffer
import com.traq.core.sensors.detector.MovementDetector
import com.traq.core.sensors.model.SensorReading
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SensorCollectorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SensorCollector {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val ringBuffer = SensorRingBuffer(maxSize = 3000)
    private val movementDetector = MovementDetector()

    private val _sensorData = MutableStateFlow<SensorReading?>(null)
    override val sensorData: StateFlow<SensorReading?> = _sensorData.asStateFlow()

    private val _isMoving = MutableStateFlow(false)
    override val isMoving: StateFlow<Boolean> = _isMoving.asStateFlow()

    private var accelValues = FloatArray(3)
    private var gyroValues: FloatArray? = null
    private var magnetValues: FloatArray? = null
    private var stepCount: Int? = null

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    accelValues = event.values.copyOf()
                    val reading = buildReading()
                    ringBuffer.add(reading)
                    _sensorData.value = reading
                    _isMoving.value = movementDetector.isMoving(reading)
                }
                Sensor.TYPE_GYROSCOPE -> gyroValues = event.values.copyOf()
                Sensor.TYPE_MAGNETIC_FIELD -> magnetValues = event.values.copyOf()
                Sensor.TYPE_STEP_COUNTER -> stepCount = event.values[0].toInt()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun start() {
        registerSensor(Sensor.TYPE_ACCELEROMETER, SensorManager.SENSOR_DELAY_GAME)
        registerSensor(Sensor.TYPE_GYROSCOPE, SensorManager.SENSOR_DELAY_GAME)
        registerSensor(Sensor.TYPE_MAGNETIC_FIELD, SensorManager.SENSOR_DELAY_NORMAL)
        registerSensor(Sensor.TYPE_STEP_COUNTER, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun stop() {
        sensorManager.unregisterListener(listener)
        ringBuffer.clear()
        movementDetector.reset()
    }

    override fun getRecentReadings(windowMs: Long): List<SensorReading> {
        val cutoff = System.currentTimeMillis() - windowMs
        return ringBuffer.getReadingsSince(cutoff)
    }

    private fun registerSensor(type: Int, delay: Int) {
        sensorManager.getDefaultSensor(type)?.let {
            sensorManager.registerListener(listener, it, delay)
        }
    }

    private fun buildReading() = SensorReading(
        timestamp = System.currentTimeMillis(),
        accelerometer = accelValues.copyOf(),
        gyroscope = gyroValues?.copyOf(),
        magnetometer = magnetValues?.copyOf(),
        stepCount = stepCount
    )
}
