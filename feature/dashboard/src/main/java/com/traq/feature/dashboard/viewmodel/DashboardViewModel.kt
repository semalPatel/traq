package com.traq.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traq.core.common.model.TripStatus
import com.traq.core.data.model.Trip
import com.traq.core.data.repository.TripRepository
import com.traq.core.location.controller.TrackingController
import com.traq.core.location.model.TrackingReadiness
import com.traq.feature.dashboard.model.DashboardTimeframe
import com.traq.feature.dashboard.model.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val trackingController: TrackingController
) : ViewModel() {

    private val selectedTimeframe = MutableStateFlow(DashboardTimeframe.WEEK)
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                tripRepository.getTripsFlow(),
                trackingController.trackingState,
                selectedTimeframe
            ) { trips, trackingState, timeframe ->
                val activeTrip = trips.find { it.status == TripStatus.ACTIVE }
                val completed = trips.filter { it.status == TripStatus.COMPLETED }
                val filteredTrips = completed.filterFor(timeframe)
                val tripCount = filteredTrips.size
                DashboardUiState(
                    activeTrip = activeTrip,
                    activeTripState = if (trackingState.isTracking) trackingState else null,
                    selectedTimeframe = timeframe,
                    filteredTripCount = tripCount,
                    filteredDistance = filteredTrips.sumOf { it.metrics.totalDistanceMeters },
                    filteredDuration = filteredTrips.sumOf { it.metrics.totalDurationMs },
                    averageDistanceMeters = if (tripCount > 0) {
                        filteredTrips.sumOf { it.metrics.totalDistanceMeters } / tripCount
                    } else {
                        0.0
                    },
                    averageDurationMs = if (tripCount > 0) {
                        filteredTrips.sumOf { it.metrics.totalDurationMs } / tripCount
                    } else {
                        0L
                    },
                    longestTripDistanceMeters = filteredTrips.maxOfOrNull { it.metrics.totalDistanceMeters } ?: 0.0,
                    totalAscentMeters = filteredTrips.sumOf { it.metrics.totalAscentMeters },
                    favoriteMode = filteredTrips
                        .mapNotNull { it.dominantMode }
                        .groupingBy { it }
                        .eachCount()
                        .maxByOrNull { it.value }
                        ?.key,
                    latestCompletedTrip = completed.maxByOrNull { it.startTime },
                    isLoading = false
                )
            }.collect { _uiState.value = it }
        }
    }

    fun updateTimeframe(timeframe: DashboardTimeframe) {
        selectedTimeframe.value = timeframe
    }

    fun getTrackingReadiness(): TrackingReadiness = trackingController.getTrackingReadiness()

    fun startNewTrip(): String = trackingController.startTrip()

    private fun List<Trip>.filterFor(timeframe: DashboardTimeframe): List<Trip> {
        val days = timeframe.days ?: return this
        val cutoff = Instant.now().minus(days, ChronoUnit.DAYS)
        return filter { it.startTime >= cutoff }
    }
}
