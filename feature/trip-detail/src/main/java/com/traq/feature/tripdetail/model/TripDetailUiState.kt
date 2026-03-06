package com.traq.feature.tripdetail.model

import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.Trip
import com.traq.core.maps.api.LatLng
import com.traq.core.maps.api.LatLngBounds
import com.traq.core.maps.api.RoutePolyline

data class TripDetailUiState(
    val trip: Trip? = null,
    val trackPoints: List<TrackPoint> = emptyList(),
    val routePolylines: List<RoutePolyline> = emptyList(),
    val mapBounds: LatLngBounds? = null,
    val showExportSheet: Boolean = false,
    val showRenameDialog: Boolean = false,
    val isLoading: Boolean = true
)
