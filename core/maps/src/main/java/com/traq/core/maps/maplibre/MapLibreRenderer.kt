package com.traq.core.maps.maplibre

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point
import javax.inject.Inject

private const val ROUTE_SOURCE_ID = "traq-route-source"
private const val ROUTE_LAYER_ID = "traq-route-layer"
private const val MARKER_SOURCE_ID = "traq-marker-source"
private const val MARKER_LAYER_ID = "traq-marker-layer"
private const val MARKER_BORDER_LAYER_ID = "traq-marker-border-layer"

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
        val isDarkTheme = isSystemInDarkTheme()
        val styleUrl = if (isDarkTheme) STYLE_URL_DARK else STYLE_URL_LIGHT

        var mapRef by remember { mutableStateOf<MapLibreMap?>(null) }
        var mapViewRef by remember { mutableStateOf<MapView?>(null) }

        LaunchedEffect(styleUrl) {
            mapRef?.setStyle(styleUrl)
        }

        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { ctx ->
                MapView(ctx).also { mv ->
                    mv.onCreate(null)
                    mv.getMapAsync { map ->
                        mapRef = map
                        map.setStyle(styleUrl) {
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

                // Render markers (current location dot)
                style.removeLayer(MARKER_BORDER_LAYER_ID)
                style.removeLayer(MARKER_LAYER_ID)
                style.removeSource(MARKER_SOURCE_ID)

                if (markers.isNotEmpty()) {
                    val markerPoints = markers.map { marker ->
                        Point.fromLngLat(marker.position.longitude, marker.position.latitude)
                    }
                    val featureCollection = FeatureCollection.fromFeatures(
                        markerPoints.map { Feature.fromGeometry(it) }
                    )
                    style.addSource(GeoJsonSource(MARKER_SOURCE_ID, featureCollection))

                    style.addLayer(
                        CircleLayer(MARKER_BORDER_LAYER_ID, MARKER_SOURCE_ID).apply {
                            setProperties(
                                PropertyFactory.circleRadius(10f),
                                PropertyFactory.circleColor(android.graphics.Color.WHITE),
                                PropertyFactory.circleStrokeWidth(0f)
                            )
                        }
                    )
                    style.addLayer(
                        CircleLayer(MARKER_LAYER_ID, MARKER_SOURCE_ID).apply {
                            setProperties(
                                PropertyFactory.circleRadius(7f),
                                PropertyFactory.circleColor(android.graphics.Color.parseColor("#2196F3")),
                                PropertyFactory.circleStrokeWidth(0f)
                            )
                        }
                    )
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
        const val STYLE_URL_LIGHT = "https://tiles.openfreemap.org/styles/liberty"
        const val STYLE_URL_DARK = "https://tiles.openfreemap.org/styles/dark"
    }
}
