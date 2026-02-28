package com.traq.core.maps.maplibre

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.traq.core.maps.api.CameraPosition
import com.traq.core.maps.api.DownloadProgress
import com.traq.core.maps.api.LatLngBounds
import com.traq.core.maps.api.MapMarker
import com.traq.core.maps.api.MapRenderer
import com.traq.core.maps.api.OfflineRegion
import com.traq.core.maps.api.RoutePolyline
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MapLibreRenderer @Inject constructor(
    @ApplicationContext private val context: Context
) : MapRenderer {

    @Composable
    override fun MapView(
        modifier: Modifier,
        cameraPosition: CameraPosition,
        polylines: List<RoutePolyline>,
        markers: List<MapMarker>,
        onCameraMove: (CameraPosition) -> Unit,
        isInteractive: Boolean
    ) {
        var mapView by remember { mutableStateOf<org.maplibre.android.maps.MapView?>(null) }

        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { ctx ->
                org.maplibre.android.maps.MapView(ctx).also { mv ->
                    mv.onCreate(null)
                    mv.getMapAsync { map ->
                        map.setStyle(STYLE_URL) { }
                        map.moveCamera(
                            org.maplibre.android.camera.CameraUpdateFactory.newCameraPosition(
                                org.maplibre.android.camera.CameraPosition.Builder()
                                    .target(org.maplibre.android.geometry.LatLng(
                                        cameraPosition.latitude, cameraPosition.longitude
                                    ))
                                    .zoom(cameraPosition.zoom.toDouble())
                                    .bearing(cameraPosition.bearing.toDouble())
                                    .tilt(cameraPosition.tilt.toDouble())
                                    .build()
                            )
                        )
                    }
                    mapView = mv
                }
            },
            update = { }
        )

        DisposableEffect(Unit) {
            onDispose { mapView?.onDestroy() }
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

    override fun isOfflineCapable(): Boolean = true

    override suspend fun downloadRegion(
        name: String, bounds: LatLngBounds, zoomRange: IntRange
    ): Flow<DownloadProgress> {
        // TODO: Implement with MapLibre OfflineManager
        return flowOf(DownloadProgress(0, 0, 0f, false))
    }

    override suspend fun deleteRegion(regionId: String) {
        // TODO: Implement with MapLibre OfflineManager
    }

    override fun getCachedRegions(): Flow<List<OfflineRegion>> = flowOf(emptyList())

    companion object {
        const val STYLE_URL = "https://demotiles.maplibre.org/style.json"
    }
}
