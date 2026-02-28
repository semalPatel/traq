package com.traq.core.ai.filter

import com.traq.core.ai.util.GeoMath
import com.traq.core.data.model.TrackPoint
import java.time.Duration
import javax.inject.Inject
import kotlin.math.abs

class GpsProcessorImpl @Inject constructor() : GpsProcessor {
    private val kalmanFilter = KalmanFilter()
    private var lastPoint: TrackPoint? = null

    override fun smooth(raw: TrackPoint): TrackPoint {
        val accuracy = raw.horizontalAccuracy ?: 10f
        val (smoothLat, smoothLon, smoothAlt) = kalmanFilter.process(
            raw.latitude, raw.longitude, raw.altitude, accuracy, raw.timestamp.toEpochMilli()
        )
        lastPoint = raw
        return raw.copy(latitude = smoothLat, longitude = smoothLon, altitude = smoothAlt)
    }

    override fun isAnomaly(point: TrackPoint, previous: TrackPoint?): Boolean {
        if (previous == null) return false
        val distance = GeoMath.haversineDistance(
            previous.latitude, previous.longitude, point.latitude, point.longitude
        )
        val dt = Duration.between(previous.timestamp, point.timestamp).toMillis() / 1000.0
        if (dt <= 0) return true
        val impliedSpeed = distance / dt
        return impliedSpeed > 83.3 ||
               (point.horizontalAccuracy ?: 0f) > 50f ||
               (point.altitude != null && previous.altitude != null &&
                abs(point.altitude!! - previous.altitude!!) > 500)
    }

    override fun reset() {
        kalmanFilter.reset()
        lastPoint = null
    }
}
