package com.traq.core.data.repository

import com.traq.core.common.model.ExportFormat
import com.traq.core.common.model.MapRendererType
import com.traq.core.common.model.TrackingAccuracy
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val mapRenderer: Flow<MapRendererType>
    val trackingAccuracy: Flow<TrackingAccuracy>
    val aiEnabled: Flow<Boolean>
    val autoExportEnabled: Flow<Boolean>
    val defaultExportFormat: Flow<ExportFormat>
    val oemGuideCompleted: Flow<Boolean>
    suspend fun setMapRenderer(type: MapRendererType)
    suspend fun setTrackingAccuracy(accuracy: TrackingAccuracy)
    suspend fun setAiEnabled(enabled: Boolean)
    suspend fun setAutoExportEnabled(enabled: Boolean)
    suspend fun setDefaultExportFormat(format: ExportFormat)
    suspend fun setOemGuideCompleted(completed: Boolean)
}
