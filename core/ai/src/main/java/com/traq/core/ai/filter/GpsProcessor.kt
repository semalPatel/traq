package com.traq.core.ai.filter

import com.traq.core.data.model.TrackPoint

interface GpsProcessor {
    fun smooth(raw: TrackPoint): TrackPoint
    fun isAnomaly(point: TrackPoint, previous: TrackPoint?): Boolean
    fun reset()
}
