package com.traq.core.ai.filter

class KalmanFilter {
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var alt: Double = 0.0
    private var variance: Float = Float.MAX_VALUE
    private var initialized = false

    fun process(measuredLat: Double, measuredLon: Double, measuredAlt: Double?, accuracy: Float, timestampMs: Long): Triple<Double, Double, Double?> {
        if (!initialized) {
            lat = measuredLat
            lon = measuredLon
            alt = measuredAlt ?: 0.0
            variance = accuracy * accuracy
            initialized = true
            return Triple(lat, lon, measuredAlt)
        }

        val processNoise = 3f
        variance += processNoise

        val kalmanGain = variance / (variance + accuracy * accuracy)
        lat += kalmanGain * (measuredLat - lat)
        lon += kalmanGain * (measuredLon - lon)
        measuredAlt?.let { alt += kalmanGain * (it - alt) }
        variance *= (1 - kalmanGain)

        return Triple(lat, lon, measuredAlt?.let { alt })
    }

    fun reset() {
        initialized = false
        variance = Float.MAX_VALUE
    }
}
