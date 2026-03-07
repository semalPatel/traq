package com.traq.core.maps.maplibre

import android.content.Context
import com.traq.core.maps.api.DownloadProgress
import com.traq.core.maps.api.LatLngBounds
import com.traq.core.maps.api.OfflineRegion
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds as MapLibreLatLngBounds
import org.maplibre.android.offline.OfflineManager
import org.maplibre.android.offline.OfflineRegion.OfflineRegionDeleteCallback
import org.maplibre.android.offline.OfflineRegion.OfflineRegionObserver
import org.maplibre.android.offline.OfflineRegion.OfflineRegionStatusCallback
import org.maplibre.android.offline.OfflineRegionError
import org.maplibre.android.offline.OfflineRegionStatus
import org.maplibre.android.offline.OfflineTilePyramidRegionDefinition
import java.nio.charset.StandardCharsets
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.max

private const val METADATA_KEY_ID = "id"
private const val METADATA_KEY_NAME = "name"
private const val METADATA_KEY_CREATED_AT = "createdAt"

@Singleton
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MapLibreOfflineRegionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val refreshToken = MutableStateFlow(0)

    fun getCachedRegions(): Flow<List<OfflineRegion>> =
        refreshToken.mapLatest { loadCachedRegions() }

    suspend fun downloadRegion(
        name: String,
        bounds: LatLngBounds,
        zoomRange: IntRange
    ): Flow<DownloadProgress> {
        val logicalId = UUID.randomUUID().toString()
        val metadata = encodeMetadata(logicalId = logicalId, name = name)
        val definition = OfflineTilePyramidRegionDefinition(
            MapLibreRenderer.STYLE_URL,
            bounds.toMapLibreBounds(),
            zoomRange.first.toDouble(),
            zoomRange.last.toDouble(),
            context.resources.displayMetrics.density
        )

        return callbackFlow {
            OfflineManager.getInstance(context).createOfflineRegion(
                definition,
                metadata,
                object : OfflineManager.CreateOfflineRegionCallback {
                    override fun onCreate(region: org.maplibre.android.offline.OfflineRegion) {
                        region.setObserver(object : OfflineRegionObserver {
                            override fun onStatusChanged(status: OfflineRegionStatus) {
                                val completedResources = status.completedResourceCount
                                val requiredResources = max(status.requiredResourceCount, completedResources)
                                val percent = if (requiredResources > 0L) {
                                    completedResources.toFloat() / requiredResources.toFloat()
                                } else {
                                    0f
                                }

                                trySend(
                                    DownloadProgress(
                                        bytesDownloaded = completedResources,
                                        totalBytes = requiredResources,
                                        percentComplete = percent,
                                        isComplete = status.isComplete
                                    )
                                )

                                if (status.isComplete) {
                                    region.setDownloadState(org.maplibre.android.offline.OfflineRegion.STATE_INACTIVE)
                                    refreshToken.update { it + 1 }
                                    close()
                                }
                            }

                            override fun onError(error: OfflineRegionError) {
                                close(IllegalStateException(error.message))
                            }

                            override fun mapboxTileCountLimitExceeded(limit: Long) {
                                close(IllegalStateException("Offline tile limit exceeded: $limit"))
                            }
                        })
                        region.setDownloadState(org.maplibre.android.offline.OfflineRegion.STATE_ACTIVE)
                    }

                    override fun onError(error: String) {
                        close(IllegalStateException(error))
                    }
                }
            )

            awaitClose { }
        }
    }

    suspend fun deleteRegion(regionId: String) {
        listOfflineRegions()
            .filter { sdkRegion ->
                decodeMetadata(sdkRegion.metadata).logicalId == regionId
            }
            .forEach { sdkRegion ->
                sdkRegion.awaitDelete()
            }
        refreshToken.update { it + 1 }
    }

    private suspend fun loadCachedRegions(): List<OfflineRegion> {
        return listOfflineRegions().map { sdkRegion ->
            val metadata = decodeMetadata(sdkRegion.metadata)
            val definition = sdkRegion.definition as? OfflineTilePyramidRegionDefinition
            val status = sdkRegion.awaitStatus()

            OfflineRegion(
                id = metadata.logicalId,
                name = metadata.name,
                bounds = definition?.bounds?.toAppBounds() ?: LatLngBounds(
                    southWest = com.traq.core.maps.api.LatLng(0.0, 0.0),
                    northEast = com.traq.core.maps.api.LatLng(0.0, 0.0)
                ),
                sizeBytes = status.completedResourceSize,
                createdAt = metadata.createdAt
            )
        }
    }

    private suspend fun listOfflineRegions(): List<org.maplibre.android.offline.OfflineRegion> =
        suspendCancellableCoroutine { continuation ->
            OfflineManager.getInstance(context).listOfflineRegions(
                object : OfflineManager.ListOfflineRegionsCallback {
                    override fun onList(offlineRegions: Array<org.maplibre.android.offline.OfflineRegion>?) {
                        continuation.resume(offlineRegions?.toList().orEmpty())
                    }

                    override fun onError(error: String) {
                        continuation.resumeWithException(IllegalStateException(error))
                    }
                }
            )
        }

    private suspend fun org.maplibre.android.offline.OfflineRegion.awaitStatus(): OfflineRegionStatus =
        suspendCancellableCoroutine { continuation ->
            getStatus(object : OfflineRegionStatusCallback {
                override fun onStatus(status: OfflineRegionStatus?) {
                    if (status != null) {
                        continuation.resume(status)
                    } else {
                        continuation.resumeWithException(IllegalStateException("Offline region status was null"))
                    }
                }

                override fun onError(error: String?) {
                    continuation.resumeWithException(IllegalStateException(error ?: "Unable to load offline region status"))
                }
            })
        }

    private suspend fun org.maplibre.android.offline.OfflineRegion.awaitDelete() =
        suspendCancellableCoroutine<Unit> { continuation ->
            delete(object : OfflineRegionDeleteCallback {
                override fun onDelete() {
                    continuation.resume(Unit)
                }

                override fun onError(error: String) {
                    continuation.resumeWithException(IllegalStateException(error))
                }
            })
        }

    private fun LatLngBounds.toMapLibreBounds(): MapLibreLatLngBounds {
        return MapLibreLatLngBounds.Builder()
            .include(LatLng(southWest.latitude, southWest.longitude))
            .include(LatLng(northEast.latitude, northEast.longitude))
            .build()
    }

    private fun MapLibreLatLngBounds.toAppBounds(): LatLngBounds {
        return LatLngBounds(
            southWest = com.traq.core.maps.api.LatLng(latitudeSouth, longitudeWest),
            northEast = com.traq.core.maps.api.LatLng(latitudeNorth, longitudeEast)
        )
    }

    private fun encodeMetadata(logicalId: String, name: String): ByteArray {
        return JSONObject()
            .put(METADATA_KEY_ID, logicalId)
            .put(METADATA_KEY_NAME, name)
            .put(METADATA_KEY_CREATED_AT, System.currentTimeMillis())
            .toString()
            .toByteArray(StandardCharsets.UTF_8)
    }

    private fun decodeMetadata(metadata: ByteArray?): OfflineRegionMetadata {
        if (metadata == null || metadata.isEmpty()) {
            return OfflineRegionMetadata(
                logicalId = UUID.randomUUID().toString(),
                name = "Offline region",
                createdAt = System.currentTimeMillis()
            )
        }

        return runCatching {
            val json = JSONObject(String(metadata, StandardCharsets.UTF_8))
            OfflineRegionMetadata(
                logicalId = json.optString(METADATA_KEY_ID).ifBlank { UUID.randomUUID().toString() },
                name = json.optString(METADATA_KEY_NAME).ifBlank { "Offline region" },
                createdAt = json.optLong(METADATA_KEY_CREATED_AT, System.currentTimeMillis())
            )
        }.getOrElse {
            OfflineRegionMetadata(
                logicalId = UUID.randomUUID().toString(),
                name = "Offline region",
                createdAt = System.currentTimeMillis()
            )
        }
    }

    private data class OfflineRegionMetadata(
        val logicalId: String,
        val name: String,
        val createdAt: Long
    )
}
