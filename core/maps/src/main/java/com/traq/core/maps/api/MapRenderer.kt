package com.traq.core.maps.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow

interface MapRenderer {
    @Composable
    fun MapView(
        modifier: Modifier,
        cameraPosition: CameraPosition,
        polylines: List<RoutePolyline>,
        markers: List<MapMarker>,
        onCameraMove: (CameraPosition) -> Unit,
        isInteractive: Boolean = true
    )

    @Composable
    fun StaticMapThumbnail(
        modifier: Modifier,
        bounds: LatLngBounds,
        polylines: List<RoutePolyline>
    )

    fun isOfflineCapable(): Boolean

    suspend fun downloadRegion(
        name: String,
        bounds: LatLngBounds,
        zoomRange: IntRange
    ): Flow<DownloadProgress>

    suspend fun deleteRegion(regionId: String)

    fun getCachedRegions(): Flow<List<OfflineRegion>>
}
