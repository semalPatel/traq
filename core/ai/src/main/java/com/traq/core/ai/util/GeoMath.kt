package com.traq.core.ai.util

import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object GeoMath {
    private const val EARTH_RADIUS = 6371000.0

    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        return EARTH_RADIUS * 2 * asin(sqrt(a))
    }

    fun bearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLon = Math.toRadians(lon2 - lon1)
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val y = sin(dLon) * cos(lat2Rad)
        val x = cos(lat1Rad) * sin(lat2Rad) - sin(lat1Rad) * cos(lat2Rad) * cos(dLon)
        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }

    fun destinationPoint(lat: Double, lon: Double, distanceMeters: Double, bearingDegrees: Double): Pair<Double, Double> {
        val d = distanceMeters / EARTH_RADIUS
        val brng = Math.toRadians(bearingDegrees)
        val lat1 = Math.toRadians(lat)
        val lon1 = Math.toRadians(lon)

        val lat2 = asin(sin(lat1) * cos(d) + cos(lat1) * sin(d) * cos(brng))
        val lon2 = lon1 + atan2(sin(brng) * sin(d) * cos(lat1), cos(d) - sin(lat1) * sin(lat2))

        return Math.toDegrees(lat2) to Math.toDegrees(lon2)
    }

    fun bearingChangeRate(bearings: List<Float>, timestamps: List<Long>): Float {
        if (bearings.size < 2) return 0f
        var totalChange = 0f
        var totalTimeMs = 0L
        for (i in 1 until bearings.size) {
            var diff = bearings[i] - bearings[i - 1]
            if (diff > 180) diff -= 360
            if (diff < -180) diff += 360
            totalChange += kotlin.math.abs(diff)
            totalTimeMs += timestamps[i] - timestamps[i - 1]
        }
        return if (totalTimeMs > 0) totalChange / (totalTimeMs / 1000f) else 0f
    }
}
