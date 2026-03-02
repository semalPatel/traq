package com.traq.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traq.core.common.model.ExportFormat
import com.traq.core.common.model.MapRendererType
import com.traq.core.common.model.TrackingAccuracy
import com.traq.core.data.repository.UserPreferencesRepository
import com.traq.core.data.util.StorageCalculator
import com.traq.core.permissions.BatteryOptimizationHelper
import com.traq.core.permissions.PermissionManager
import com.traq.feature.settings.model.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: UserPreferencesRepository,
    private val permissionManager: PermissionManager,
    private val batteryHelper: BatteryOptimizationHelper,
    private val storageCalculator: StorageCalculator
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                prefs.trackingAccuracy,
                prefs.aiEnabled,
                prefs.mapRenderer,
                prefs.defaultExportFormat,
                prefs.autoExportEnabled
            ) { accuracy, ai, map, export, autoExport ->
                SettingsUiState(
                    trackingAccuracy = accuracy,
                    aiEnabled = ai,
                    mapRenderer = map,
                    defaultExportFormat = export,
                    autoExportEnabled = autoExport,
                    isBatteryOptimized = permissionManager.isBatteryOptimizationDisabled(),
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
        refreshStorageInfo()
    }

    fun setTrackingAccuracy(accuracy: TrackingAccuracy) {
        viewModelScope.launch { prefs.setTrackingAccuracy(accuracy) }
    }

    fun setAiEnabled(enabled: Boolean) {
        viewModelScope.launch { prefs.setAiEnabled(enabled) }
    }

    fun setMapRenderer(type: MapRendererType) {
        viewModelScope.launch { prefs.setMapRenderer(type) }
    }

    fun setExportFormat(format: ExportFormat) {
        viewModelScope.launch { prefs.setDefaultExportFormat(format) }
    }

    fun setAutoExport(enabled: Boolean) {
        viewModelScope.launch { prefs.setAutoExportEnabled(enabled) }
    }

    fun requestBatteryOptimization() {
        batteryHelper.requestDisableBatteryOptimization()
    }

    fun openBatterySettings() {
        batteryHelper.openBatterySettings()
    }

    fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            storageCalculator.clearCache()
            refreshStorageInfo()
        }
    }

    private fun refreshStorageInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val dbBytes = storageCalculator.getDatabaseSizeBytes()
            val cacheBytes = storageCalculator.getCacheSizeBytes()
            _uiState.update {
                it.copy(
                    databaseSizeMb = formatSize(dbBytes),
                    cacheSizeMb = formatSize(cacheBytes)
                )
            }
        }
    }

    private fun formatSize(bytes: Long): String = when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "%.1f KB".format(bytes / 1024.0)
        else -> "%.1f MB".format(bytes / (1024.0 * 1024.0))
    }
}
