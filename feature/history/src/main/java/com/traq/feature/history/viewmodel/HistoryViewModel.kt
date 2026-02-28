package com.traq.feature.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traq.core.common.model.TripStatus
import com.traq.core.data.repository.TripRepository
import com.traq.feature.history.model.HistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tripRepository.getTripsFlow().collect { trips ->
                val completed = trips.filter { it.status == TripStatus.COMPLETED }
                _uiState.update { it.copy(trips = completed, isLoading = false) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun deleteTrip(tripId: String) {
        viewModelScope.launch { tripRepository.deleteTrip(tripId) }
    }
}
