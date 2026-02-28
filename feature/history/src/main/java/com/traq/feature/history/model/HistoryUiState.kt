package com.traq.feature.history.model

import com.traq.core.data.model.Trip

data class HistoryUiState(
    val trips: List<Trip> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true
)
