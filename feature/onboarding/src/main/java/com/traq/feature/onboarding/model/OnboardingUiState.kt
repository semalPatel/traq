package com.traq.feature.onboarding.model

import com.traq.core.permissions.PermissionState

data class OnboardingUiState(
    val currentPage: Int = 0,
    val totalPages: Int = 6,
    val permissionState: PermissionState = PermissionState(),
    val oemType: String = "GENERIC",
    val oemInstructions: String = "",
    val needsOemGuide: Boolean = false
)
