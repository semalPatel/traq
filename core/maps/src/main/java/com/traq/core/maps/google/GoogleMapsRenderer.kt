package com.traq.core.maps.google

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition as GmsCameraPosition
import com.google.android.gms.maps.model.LatLng as GmsLatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.traq.core.maps.api.CameraPosition
import com.traq.core.maps.api.DownloadProgress
import com.traq.core.maps.api.LatLngBounds
import com.traq.core.maps.api.MapMarker
import com.traq.core.maps.api.MapRenderer
import com.traq.core.maps.api.OfflineRegion
import com.traq.core.maps.api.RoutePolyline
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GoogleMapsRenderer @Inject constructor() : MapRenderer {

    @Composable
    override fun MapView(
        modifier: Modifier,
        cameraPosition: CameraPosition,
        polylines: List<RoutePolyline>,
        markers: List<MapMarker>,
        onCameraMove: (CameraPosition) -> Unit,
        isInteractive: Boolean
    ) {
        val camState = rememberCameraPositionState {
            position = GmsCameraPosition.Builder()
                .target(GmsLatLng(cameraPosition.latitude, cameraPosition.longitude))
                .zoom(cameraPosition.zoom)
                .bearing(cameraPosition.bearing)
                .tilt(cameraPosition.tilt)
                .build()
        }

        LaunchedEffect(cameraPosition) {
            val update = CameraUpdateFactory.newCameraPosition(
                GmsCameraPosition.Builder()
                    .target(GmsLatLng(cameraPosition.latitude, cameraPosition.longitude))
                    .zoom(cameraPosition.zoom)
                    .bearing(cameraPosition.bearing)
                    .tilt(cameraPosition.tilt)
                    .build()
            )
            camState.animate(update)
        }

        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = camState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                scrollGesturesEnabled = isInteractive,
                zoomGesturesEnabled = isInteractive,
                rotationGesturesEnabled = isInteractive,
                tiltGesturesEnabled = isInteractive
            ),
            properties = MapProperties(mapType = MapType.NORMAL)
        ) {
            polylines.forEach { polyline ->
                Polyline(
                    points = polyline.points.map { GmsLatLng(it.latitude, it.longitude) },
                    color = Color(polyline.colorInt),
                    width = polyline.widthDp
                )
            }

            markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = GmsLatLng(marker.position.latitude, marker.position.longitude)),
                    title = marker.title,
                    snippet = marker.snippet
                )
            }
        }
    }

    @Composable
    override fun StaticMapThumbnail(
        modifier: Modifier,
        bounds: LatLngBounds,
        polylines: List<RoutePolyline>
    ) {
        MapView(
            modifier = modifier,
            cameraPosition = CameraPosition(bounds.center.latitude, bounds.center.longitude, 12f, 0f),
            polylines = polylines,
            markers = emptyList(),
            onCameraMove = {},
            isInteractive = false
        )
    }

    override fun isOfflineCapable(): Boolean = false

    override suspend fun downloadRegion(
        name: String, bounds: LatLngBounds, zoomRange: IntRange
    ): Flow<DownloadProgress> {
        throw UnsupportedOperationException("Google Maps does not support offline tile downloads")
    }

    override suspend fun deleteRegion(regionId: String) {
        throw UnsupportedOperationException("Google Maps does not support offline tile management")
    }

    override fun getCachedRegions(): Flow<List<OfflineRegion>> = flowOf(emptyList())
}
