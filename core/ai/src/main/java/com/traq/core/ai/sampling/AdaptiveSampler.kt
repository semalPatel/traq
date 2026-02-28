package com.traq.core.ai.sampling

interface AdaptiveSampler {
    fun getRecommendedIntervalMs(
        currentSpeed: Float?,
        bearingChangeRate: Float?,
        accuracy: Float?,
        batteryPercent: Int,
        isMoving: Boolean
    ): Long
}
