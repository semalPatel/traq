package com.traq.core.location.model

enum class TrackingRequirement(val displayName: String) {
    FOREGROUND_LOCATION("Precise location"),
    BACKGROUND_LOCATION("Background location"),
    NOTIFICATIONS("Notifications"),
    BATTERY_OPTIMIZATION("Battery optimization exemption")
}

data class TrackingReadiness(
    val hasForegroundLocation: Boolean,
    val hasBackgroundLocation: Boolean,
    val hasNotifications: Boolean,
    val isBatteryOptimizationDisabled: Boolean
) {
    val blockingRequirements: List<TrackingRequirement>
        get() = buildList {
            if (!hasForegroundLocation) add(TrackingRequirement.FOREGROUND_LOCATION)
            if (!hasBackgroundLocation) add(TrackingRequirement.BACKGROUND_LOCATION)
            if (!hasNotifications) add(TrackingRequirement.NOTIFICATIONS)
        }

    val warningRequirements: List<TrackingRequirement>
        get() = buildList {
            if (!isBatteryOptimizationDisabled) add(TrackingRequirement.BATTERY_OPTIMIZATION)
        }

    val canStartTrip: Boolean
        get() = blockingRequirements.isEmpty()
}
