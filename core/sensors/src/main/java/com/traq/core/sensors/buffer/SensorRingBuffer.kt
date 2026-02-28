package com.traq.core.sensors.buffer

import com.traq.core.sensors.model.SensorReading
import java.util.concurrent.locks.ReentrantReadWriteLock

class SensorRingBuffer(private val maxSize: Int = 3000) {
    private val buffer = ArrayDeque<SensorReading>(maxSize)
    private val lock = ReentrantReadWriteLock()

    fun add(reading: SensorReading) {
        lock.writeLock().lock()
        try {
            if (buffer.size >= maxSize) buffer.removeFirst()
            buffer.addLast(reading)
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun getReadingsSince(timestampMs: Long): List<SensorReading> {
        lock.readLock().lock()
        try {
            return buffer.filter { it.timestamp >= timestampMs }
        } finally {
            lock.readLock().unlock()
        }
    }

    fun clear() {
        lock.writeLock().lock()
        try { buffer.clear() }
        finally { lock.writeLock().unlock() }
    }

    fun getAll(): List<SensorReading> {
        lock.readLock().lock()
        try { return buffer.toList() }
        finally { lock.readLock().unlock() }
    }
}
