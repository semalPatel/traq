package com.traq.core.ai.sampling

import javax.inject.Inject

class RuleBasedSampler @Inject constructor() : AdaptiveSampler {

    override fun getRecommendedIntervalMs(
        currentSpeed: Float?,
        bearingChangeRate: Float?,
        accuracy: Float?,
        batteryPercent: Int,
        isMoving: Boolean
    ): Long {
        if (!isMoving) return 30_000L

        val speed = currentSpeed ?: return 3_000L

        val baseInterval = when {
            speed < 1.5f -> 5_000L
            speed < 4f -> 3_000L
            speed < 10f -> 2_000L
            speed < 25f -> 3_000L
            else -> 5_000L
        }

        val curveMultiplier = when {
            (bearingChangeRate ?: 0f) > 15f -> 0.5
            (bearingChangeRate ?: 0f) > 5f -> 0.75
            else -> 1.0
        }

        val batteryMultiplier = when {
            batteryPercent < 10 -> 3.0
            batteryPercent < 20 -> 2.0
            batteryPercent < 30 -> 1.5
            else -> 1.0
        }

        val accuracyMultiplier = when {
            (accuracy ?: 5f) > 30f -> 0.5
            (accuracy ?: 5f) > 15f -> 0.75
            else -> 1.0
        }

        val interval = (baseInterval * curveMultiplier * batteryMultiplier * accuracyMultiplier).toLong()
        return interval.coerceIn(1_000L, 60_000L)
    }
}
