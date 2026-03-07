package com.traq.feature.tripdetail.viewmodel

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traq.core.common.model.ExportFormat
import com.traq.core.data.repository.TrackPointRepository
import com.traq.core.data.repository.TripRepository
import com.traq.core.export.api.ExportManager
import com.traq.core.maps.api.LatLng
import com.traq.core.maps.api.LatLngBounds
import com.traq.core.maps.api.RoutePolyline
import com.traq.core.ui.util.ColorUtils
import com.traq.feature.tripdetail.model.TripDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val tripRepository: TripRepository,
    private val trackPointRepository: TrackPointRepository,
    private val exportManager: ExportManager
) : ViewModel() {

    private val tripId: String = savedStateHandle["tripId"] ?: ""

    private val _uiState = MutableStateFlow(TripDetailUiState())
    val uiState: StateFlow<TripDetailUiState> = _uiState.asStateFlow()

    private val _shareIntent = MutableSharedFlow<Intent>()
    val shareIntent: SharedFlow<Intent> = _shareIntent.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                tripRepository.getTripFlow(tripId),
                trackPointRepository.getTrackPointsFlow(tripId)
            ) { trip, points ->
                val latLngs = points.map { LatLng(it.latitude, it.longitude) }
                val polyline = if (latLngs.size >= 2) {
                    val color = trip?.dominantMode?.let { ColorUtils.transportModeColor(it).hashCode() }
                        ?: 0xFF00BFA5.toInt()
                    listOf(RoutePolyline(points = latLngs, colorInt = color, widthDp = 5f))
                } else emptyList()

                val bounds = if (latLngs.isNotEmpty()) {
                    LatLngBounds(
                        southWest = LatLng(latLngs.minOf { it.latitude }, latLngs.minOf { it.longitude }),
                        northEast = LatLng(latLngs.maxOf { it.latitude }, latLngs.maxOf { it.longitude })
                    )
                } else null

                TripDetailUiState(
                    trip = trip,
                    trackPoints = points,
                    routePolylines = polyline,
                    mapBounds = bounds,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.update { old ->
                    newState.copy(
                        showExportSheet = old.showExportSheet,
                        showRenameDialog = old.showRenameDialog,
                        showDeleteDialog = old.showDeleteDialog,
                        tripDeleted = old.tripDeleted
                    )
                }
            }
        }
    }

    fun showRenameDialog() {
        _uiState.update { it.copy(showRenameDialog = true) }
    }

    fun dismissRenameDialog() {
        _uiState.update { it.copy(showRenameDialog = false) }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun renameTrip(name: String) {
        viewModelScope.launch {
            if (name.isNotBlank()) {
                tripRepository.renameTrip(tripId, name.trim())
            }
            _uiState.update { it.copy(showRenameDialog = false) }
        }
    }

    fun deleteTrip() {
        viewModelScope.launch {
            tripRepository.deleteTrip(tripId)
            _uiState.update {
                it.copy(
                    showDeleteDialog = false,
                    tripDeleted = true
                )
            }
        }
    }

    fun toggleExportSheet() {
        _uiState.update { it.copy(showExportSheet = !it.showExportSheet) }
    }

    fun exportTrip(format: ExportFormat) {
        viewModelScope.launch {
            try {
                val intent = exportManager.exportTripToShare(tripId, format)
                _shareIntent.emit(intent)
            } catch (e: Exception) {
                // TODO: expose error to UI
            }
            _uiState.update { it.copy(showExportSheet = false) }
        }
    }

    fun getSupportedFormats(): List<ExportFormat> = ExportFormat.entries.toList()
}
