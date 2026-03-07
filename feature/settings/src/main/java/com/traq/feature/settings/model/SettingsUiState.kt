package com.traq.feature.settings.model

import com.traq.core.common.model.ExportFormat
import com.traq.core.common.model.TrackingAccuracy
import com.traq.core.maps.api.OfflineRegion

data class SettingsUiState(
    val trackingAccuracy: TrackingAccuracy = TrackingAccuracy.HIGH,
    val aiEnabled: Boolean = true,
    val defaultExportFormat: ExportFormat = ExportFormat.TRAQ,
    val autoExportEnabled: Boolean = false,
    val isBatteryOptimized: Boolean = false,
    val isOfflineMapsSupported: Boolean = false,
    val offlineRegions: List<OfflineRegion> = emptyList(),
    val offlineMapsSizeMb: String = "--",
    val isDownloadingOfflineRegion: Boolean = false,
    val offlineDownloadPercent: Int? = null,
    val offlineStatusMessage: String? = null,
    val databaseSizeMb: String = "--",
    val cacheSizeMb: String = "--",
    val isLoading: Boolean = true
)
