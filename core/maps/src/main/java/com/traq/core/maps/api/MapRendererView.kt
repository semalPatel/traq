package com.traq.core.maps.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RenderMapView(
    renderer: MapRenderer,
    modifier: Modifier,
    cameraPosition: CameraPosition,
    polylines: List<RoutePolyline>,
    markers: List<MapMarker>,
    onCameraMove: (CameraPosition) -> Unit,
    isInteractive: Boolean = true
) {
    renderer.MapView(
        modifier = modifier,
        cameraPosition = cameraPosition,
        polylines = polylines,
        markers = markers,
        onCameraMove = onCameraMove,
        isInteractive = isInteractive
    )
}
