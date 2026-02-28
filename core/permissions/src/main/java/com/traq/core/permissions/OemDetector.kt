package com.traq.core.permissions

import android.os.Build

object OemDetector {
    enum class OemType { SAMSUNG, XIAOMI, HUAWEI, ONEPLUS, OPPO, VIVO, REALME, GENERIC }

    fun detect(): OemType {
        val manufacturer = Build.MANUFACTURER.lowercase()
        return when {
            manufacturer.contains("samsung") -> OemType.SAMSUNG
            manufacturer.contains("xiaomi") || manufacturer.contains("redmi") -> OemType.XIAOMI
            manufacturer.contains("huawei") || manufacturer.contains("honor") -> OemType.HUAWEI
            manufacturer.contains("oneplus") -> OemType.ONEPLUS
            manufacturer.contains("oppo") -> OemType.OPPO
            manufacturer.contains("vivo") -> OemType.VIVO
            manufacturer.contains("realme") -> OemType.REALME
            else -> OemType.GENERIC
        }
    }

    fun needsOemGuide(): Boolean = detect() != OemType.GENERIC

    fun getOemInstructions(): String = when (detect()) {
        OemType.SAMSUNG -> "Go to Settings > Battery > Background usage limits > Remove Traq from sleeping apps. Also disable Adaptive Battery."
        OemType.XIAOMI -> "Go to Settings > Battery > App battery saver > Set Traq to 'No restrictions'. Also enable Autostart for Traq."
        OemType.HUAWEI -> "Go to Settings > Battery > App launch > Find Traq > Disable 'Manage automatically' > Enable all three toggles."
        OemType.ONEPLUS -> "Go to Settings > Battery > Battery optimization > Find Traq > Set to 'Don't optimize'."
        OemType.OPPO -> "Go to Settings > Battery > App power consumption > Traq > Enable 'Allow background running'."
        OemType.VIVO -> "Go to Settings > Battery > High background power consumption > Allow Traq."
        OemType.REALME -> "Go to Settings > Battery > App power consumption > Traq > Enable 'Allow background activity'."
        OemType.GENERIC -> "Go to Settings > Battery > Battery Optimization > Find Traq > Set to 'Not optimized'."
    }
}
