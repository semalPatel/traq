package com.traq.feature.tracking.model

import com.traq.core.location.model.TrackingState
import com.traq.core.maps.api.CameraPosition
import com.traq.core.maps.api.LatLng

data class TrackingUiState(
    val tripId: String = "",
    val trackingState: TrackingState = TrackingState.IDLE,
    val routePoints: List<LatLng> = emptyList(),
    val currentPosition: LatLng? = null,
    val cameraPosition: CameraPosition = CameraPosition(0.0, 0.0, 15f, 0f),
    val showStopConfirmation: Boolean = false,
    val isMapReady: Boolean = false
)
