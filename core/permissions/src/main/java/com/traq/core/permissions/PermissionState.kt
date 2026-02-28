package com.traq.core.permissions

data class PermissionState(
    val location: Boolean = false,
    val backgroundLocation: Boolean = false,
    val activityRecognition: Boolean = false,
    val notifications: Boolean = false,
    val batteryOptimization: Boolean = false,
    val oemBatteryHandled: Boolean = false
) {
    val allCriticalGranted: Boolean
        get() = location && backgroundLocation && batteryOptimization
    val isComplete: Boolean
        get() = allCriticalGranted && oemBatteryHandled
}
