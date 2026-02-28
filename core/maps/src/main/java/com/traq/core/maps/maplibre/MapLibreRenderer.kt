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
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point
import javax.inject.Inject

private const val ROUTE_SOURCE_ID = "traq-route-source"
private const val ROUTE_LAYER_ID = "traq-route-layer"

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
        var mapRef by remember { mutableStateOf<MapLibreMap?>(null) }
        var mapViewRef by remember { mutableStateOf<MapView?>(null) }

        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { ctx ->
                MapView(ctx).also { mv ->
                    mv.onCreate(null)
                    mv.getMapAsync { map ->
                        mapRef = map
                        map.setStyle(STYLE_URL) {
                            map.moveCamera(
                                CameraUpdateFactory.newCameraPosition(
                                    org.maplibre.android.camera.CameraPosition.Builder()
                                        .target(LatLng(cameraPosition.latitude, cameraPosition.longitude))
                                        .zoom(cameraPosition.zoom.toDouble())
                                        .bearing(cameraPosition.bearing.toDouble())
                                        .tilt(cameraPosition.tilt.toDouble())
                                        .build()
                                )
                            )
                        }
                    }
                    mapViewRef = mv
                }
            },
            update = { _ ->
                val map = mapRef ?: return@AndroidView

                map.animateCamera(
                    CameraUpdateFactory.newCameraPosition(
                        org.maplibre.android.camera.CameraPosition.Builder()
                            .target(LatLng(cameraPosition.latitude, cameraPosition.longitude))
                            .zoom(cameraPosition.zoom.toDouble())
                            .bearing(cameraPosition.bearing.toDouble())
                            .build()
                    ),
                    300
                )

                val style = map.style ?: return@AndroidView

                style.removeLayer(ROUTE_LAYER_ID)
                style.removeSource(ROUTE_SOURCE_ID)

                polylines.forEach { polyline ->
                    if (polyline.points.size >= 2) {
                        val coords = polyline.points.map {
                            Point.fromLngLat(it.longitude, it.latitude)
                        }
                        val lineString = LineString.fromLngLats(coords)

                        style.addSource(GeoJsonSource(ROUTE_SOURCE_ID, lineString))
                        style.addLayer(
                            LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID).apply {
                                setProperties(
                                    PropertyFactory.lineColor(polyline.colorInt),
                                    PropertyFactory.lineWidth(polyline.widthDp),
                                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND)
                                )
                            }
                        )
                    }
                }
            }
        )

        DisposableEffect(Unit) {
            onDispose {
                mapViewRef?.onDestroy()
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

    override fun isOfflineCapable(): Boolean = true

    override suspend fun downloadRegion(
        name: String, bounds: LatLngBounds, zoomRange: IntRange
    ): Flow<DownloadProgress> {
        return flowOf(DownloadProgress(0, 0, 0f, false))
    }

    override suspend fun deleteRegion(regionId: String) {}

    override fun getCachedRegions(): Flow<List<OfflineRegion>> = flowOf(emptyList())

    companion object {
        const val STYLE_URL = "https://demotiles.maplibre.org/style.json"
    }
}
