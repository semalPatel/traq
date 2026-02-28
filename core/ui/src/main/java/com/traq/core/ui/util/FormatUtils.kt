package com.traq.core.ui.util

import kotlin.math.roundToInt

object FormatUtils {
    fun formatDistance(meters: Double): String {
        return if (meters < 1000) "${meters.roundToInt()} m"
        else "%.1f km".format(meters / 1000)
    }

    fun formatSpeed(mps: Float): String = "%.1f km/h".format(mps * 3.6f)

    fun formatDuration(ms: Long): String {
        val hours = ms / 3_600_000
        val minutes = (ms % 3_600_000) / 60_000
        val seconds = (ms % 60_000) / 1_000
        return if (hours > 0) "%d:%02d:%02d".format(hours, minutes, seconds)
        else "%02d:%02d".format(minutes, seconds)
    }

    fun formatElevation(meters: Double): String = "${meters.roundToInt()} m"
}
