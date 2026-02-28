package com.traq.feature.onboarding.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.traq.core.data.repository.UserPreferencesRepository
import com.traq.core.permissions.OemDetector
import com.traq.core.permissions.PermissionManager
import com.traq.feature.onboarding.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val permissionManager: PermissionManager,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        val needsOem = OemDetector.needsOemGuide()
        var pages = 4 // Welcome + Location + Background + Battery + Done
        if (Build.VERSION.SDK_INT >= 29) pages++ // Activity Recognition
        if (Build.VERSION.SDK_INT >= 33) pages++ // Notifications
        if (needsOem) pages++ // OEM guide

        _uiState.value = OnboardingUiState(
            totalPages = pages,
            oemType = OemDetector.detect().name,
            oemInstructions = OemDetector.getOemInstructions(),
            needsOemGuide = needsOem
        )
        refreshPermissionState()
    }

    fun onPermissionResult() {
        refreshPermissionState()
    }

    fun nextPage() {
        _uiState.update { it.copy(currentPage = (it.currentPage + 1).coerceAtMost(it.totalPages - 1)) }
    }

    fun markOemGuideCompleted() {
        viewModelScope.launch {
            prefsRepository.setOemGuideCompleted(true)
            refreshPermissionState()
        }
    }

    fun isOnboardingNeeded(): Boolean {
        val state = permissionManager.getPermissionState()
        return !state.allCriticalGranted
    }

    private fun refreshPermissionState() {
        viewModelScope.launch {
            val oemCompleted = prefsRepository.oemGuideCompleted.first()
            _uiState.update {
                it.copy(permissionState = permissionManager.getPermissionState(oemCompleted))
            }
        }
    }
}
