package com.traq.feature.tracking.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traq.core.data.repository.TrackPointRepository
import com.traq.core.location.controller.TrackingController
import com.traq.core.maps.api.CameraPosition
import com.traq.core.maps.api.LatLng
import com.traq.feature.tracking.model.TrackingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val trackingController: TrackingController,
    private val trackPointRepository: TrackPointRepository
) : ViewModel() {

    private val tripId: String = savedStateHandle["tripId"] ?: ""

    private val _uiState = MutableStateFlow(TrackingUiState(tripId = tripId))
    val uiState: StateFlow<TrackingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val lastLocation = trackingController.getLastLocation()
            if (lastLocation != null) {
                _uiState.update {
                    it.copy(cameraPosition = CameraPosition(lastLocation.latitude, lastLocation.longitude, 16f, 0f))
                }
            }
        }
        viewModelScope.launch {
            trackingController.trackingState.collect { state ->
                _uiState.update { it.copy(trackingState = state) }
            }
        }
        viewModelScope.launch {
            trackPointRepository.getTrackPointsFlow(tripId).collect { points ->
                val latLngs = points.map { LatLng(it.latitude, it.longitude) }
                val current = latLngs.lastOrNull()
                _uiState.update {
                    it.copy(
                        routePoints = latLngs,
                        currentPosition = current,
                        cameraPosition = current?.let { pos ->
                            CameraPosition(pos.latitude, pos.longitude, 16f, 0f)
                        } ?: it.cameraPosition
                    )
                }
            }
        }
    }

    fun onPause() = trackingController.pauseTrip()
    fun onResume() = trackingController.resumeTrip()
    fun onRequestStop() { _uiState.update { it.copy(showStopConfirmation = true) } }
    fun onDismissStop() { _uiState.update { it.copy(showStopConfirmation = false) } }
    fun onConfirmStop() {
        trackingController.stopTrip()
        _uiState.update { it.copy(showStopConfirmation = false) }
    }
}
