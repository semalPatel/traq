package com.traq.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traq.core.common.model.ExportFormat
import com.traq.core.common.model.TrackingAccuracy
import com.traq.core.data.repository.UserPreferencesRepository
import com.traq.core.data.util.StorageCalculator
import com.traq.core.location.controller.TrackingController
import com.traq.core.maps.api.LatLng
import com.traq.core.maps.api.LatLngBounds
import com.traq.core.maps.api.MapRenderer
import com.traq.core.maps.maplibre.MapLibreRenderer
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
import kotlin.math.abs
import kotlin.math.cos

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: UserPreferencesRepository,
    private val permissionManager: PermissionManager,
    private val batteryHelper: BatteryOptimizationHelper,
    private val storageCalculator: StorageCalculator,
    private val mapRenderer: MapRenderer,
    private val trackingController: TrackingController
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                prefs.trackingAccuracy,
                prefs.aiEnabled,
                prefs.defaultExportFormat,
                prefs.autoExportEnabled
            ) { accuracy, ai, export, autoExport ->
                SettingsUiState(
                    trackingAccuracy = accuracy,
                    aiEnabled = ai,
                    defaultExportFormat = export,
                    autoExportEnabled = autoExport,
                    isBatteryOptimized = permissionManager.isBatteryOptimizationDisabled(),
                    isOfflineMapsSupported = mapRenderer.isOfflineCapable(),
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
        viewModelScope.launch {
            mapRenderer.getCachedRegions().collect { regions ->
                _uiState.update {
                    it.copy(
                        offlineRegions = regions,
                        offlineMapsSizeMb = formatSize(regions.sumOf { region -> region.sizeBytes })
                    )
                }
            }
        }
        refreshStorageInfo()
    }

    fun setTrackingAccuracy(accuracy: TrackingAccuracy) {
        viewModelScope.launch { prefs.setTrackingAccuracy(accuracy) }
    }

    fun setAiEnabled(enabled: Boolean) {
        viewModelScope.launch { prefs.setAiEnabled(enabled) }
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

    fun downloadOfflineRegionAroundLastLocation() {
        viewModelScope.launch {
            val lastLocation = trackingController.getLastLocation()
            if (lastLocation == null) {
                _uiState.update {
                    it.copy(offlineStatusMessage = "No recent location available yet. Open the map or start a trip first.")
                }
                return@launch
            }

            val regionName = "Offline area ${lastLocation.latitude.formatCoordinate()}, ${lastLocation.longitude.formatCoordinate()}"
            val bounds = buildBounds(
                latitude = lastLocation.latitude,
                longitude = lastLocation.longitude,
                radiusKm = 12.0
            )

            runCatching {
                mapRenderer.downloadRegion(
                    name = regionName,
                    bounds = bounds,
                    zoomRange = MapLibreRenderer.DEFAULT_OFFLINE_MIN_ZOOM..MapLibreRenderer.DEFAULT_OFFLINE_MAX_ZOOM
                ).collect { progress ->
                    _uiState.update {
                        it.copy(
                            isDownloadingOfflineRegion = !progress.isComplete,
                            offlineDownloadPercent = (progress.percentComplete * 100).toInt(),
                            offlineStatusMessage = if (progress.isComplete) {
                                "Offline region downloaded successfully."
                            } else {
                                "Downloading offline map: ${(progress.percentComplete * 100).toInt()}%"
                            }
                        )
                    }
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isDownloadingOfflineRegion = false,
                        offlineDownloadPercent = null,
                        offlineStatusMessage = error.message ?: "Offline map download failed."
                    )
                }
            }
        }
    }

    fun deleteOfflineRegion(regionId: String) {
        viewModelScope.launch {
            runCatching {
                mapRenderer.deleteRegion(regionId)
            }.onSuccess {
                _uiState.update {
                    it.copy(offlineStatusMessage = "Offline region deleted.")
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(offlineStatusMessage = error.message ?: "Unable to delete offline region.")
                }
            }
        }
    }

    fun clearOfflineStatusMessage() {
        _uiState.update { it.copy(offlineStatusMessage = null) }
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

    private fun buildBounds(latitude: Double, longitude: Double, radiusKm: Double): LatLngBounds {
        val latDelta = radiusKm / 111.0
        val cosLatitude = abs(cos(Math.toRadians(latitude))).coerceAtLeast(0.1)
        val lonDelta = radiusKm / (111.0 * cosLatitude)
        return LatLngBounds(
            southWest = LatLng(latitude - latDelta, longitude - lonDelta),
            northEast = LatLng(latitude + latDelta, longitude + lonDelta)
        )
    }

    private fun Double.formatCoordinate(): String = "%.2f".format(this)
}
