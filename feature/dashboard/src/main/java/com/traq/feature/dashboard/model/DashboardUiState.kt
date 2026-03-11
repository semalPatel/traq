package com.traq.feature.dashboard.model

import com.traq.core.common.model.TransportMode
import com.traq.core.data.model.Trip
import com.traq.core.location.model.TrackingState

enum class DashboardTimeframe(val label: String, val days: Long?) {
    WEEK("7D", 7),
    MONTH("30D", 30),
    QUARTER("90D", 90),
    ALL("All", null)
}

data class DashboardUiState(
    val activeTrip: Trip? = null,
    val activeTripState: TrackingState? = null,
    val selectedTimeframe: DashboardTimeframe = DashboardTimeframe.WEEK,
    val filteredTripCount: Int = 0,
    val filteredDistance: Double = 0.0,
    val filteredDuration: Long = 0,
    val averageDistanceMeters: Double = 0.0,
    val averageDurationMs: Long = 0L,
    val longestTripDistanceMeters: Double = 0.0,
    val totalAscentMeters: Double = 0.0,
    val favoriteMode: TransportMode? = null,
    val latestCompletedTrip: Trip? = null,
    val isLoading: Boolean = true
)
