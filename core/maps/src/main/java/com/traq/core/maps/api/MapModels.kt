package com.traq.core.maps.api

data class CameraPosition(
    val latitude: Double,
    val longitude: Double,
    val zoom: Float,
    val bearing: Float = 0f,
    val tilt: Float = 0f
)

data class LatLng(val latitude: Double, val longitude: Double)

data class LatLngBounds(val southWest: LatLng, val northEast: LatLng) {
    val center: LatLng get() = LatLng(
        (southWest.latitude + northEast.latitude) / 2,
        (southWest.longitude + northEast.longitude) / 2
    )
}

data class RoutePolyline(
    val points: List<LatLng>,
    val colorInt: Int,
    val widthDp: Float = 4f,
    val zIndex: Float = 0f
)

data class MapMarker(
    val position: LatLng,
    val title: String? = null,
    val snippet: String? = null,
    val iconRes: Int? = null
)

data class OfflineRegion(
    val id: String,
    val name: String,
    val bounds: LatLngBounds,
    val sizeBytes: Long,
    val createdAt: Long = System.currentTimeMillis()
)

data class DownloadProgress(
    val bytesDownloaded: Long,
    val totalBytes: Long,
    val percentComplete: Float = if (totalBytes > 0) bytesDownloaded.toFloat() / totalBytes else 0f,
    val isComplete: Boolean
)
