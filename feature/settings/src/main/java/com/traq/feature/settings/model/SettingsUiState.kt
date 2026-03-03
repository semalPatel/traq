package com.traq.feature.settings.model

import com.traq.core.common.model.ExportFormat
import com.traq.core.common.model.TrackingAccuracy

data class SettingsUiState(
    val trackingAccuracy: TrackingAccuracy = TrackingAccuracy.HIGH,
    val aiEnabled: Boolean = true,
    val defaultExportFormat: ExportFormat = ExportFormat.TRAQ,
    val autoExportEnabled: Boolean = false,
    val isBatteryOptimized: Boolean = false,
    val databaseSizeMb: String = "--",
    val cacheSizeMb: String = "--",
    val isLoading: Boolean = true
)
