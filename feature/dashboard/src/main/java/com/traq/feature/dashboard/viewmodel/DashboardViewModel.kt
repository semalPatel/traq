package com.traq.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traq.core.common.model.TripStatus
import com.traq.core.data.repository.TripRepository
import com.traq.core.location.controller.TrackingController
import com.traq.feature.dashboard.model.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val trackingController: TrackingController
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                tripRepository.getTripsFlow(),
                trackingController.trackingState
            ) { trips, trackingState ->
                val activeTrip = trips.find { it.status == TripStatus.ACTIVE }
                val completed = trips.filter { it.status == TripStatus.COMPLETED }
                DashboardUiState(
                    activeTrip = activeTrip,
                    activeTripState = if (trackingState.isTracking) trackingState else null,
                    recentTrips = completed.take(5),
                    weeklyDistance = completed.sumOf { it.metrics.totalDistanceMeters },
                    weeklyDuration = completed.sumOf { it.metrics.totalDurationMs },
                    weeklyTripCount = completed.size,
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    fun hasLocationPermission(): Boolean = trackingController.hasRequiredPermissions()

    fun startNewTrip(): String = trackingController.startTrip()
}
