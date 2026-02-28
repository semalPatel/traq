package com.traq.feature.dashboard.model

import com.traq.core.data.model.Trip
import com.traq.core.location.model.TrackingState

data class DashboardUiState(
    val activeTrip: Trip? = null,
    val activeTripState: TrackingState? = null,
    val recentTrips: List<Trip> = emptyList(),
    val weeklyDistance: Double = 0.0,
    val weeklyDuration: Long = 0,
    val weeklyTripCount: Int = 0,
    val isLoading: Boolean = true
)
