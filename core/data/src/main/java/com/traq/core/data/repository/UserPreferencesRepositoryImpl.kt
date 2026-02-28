package com.traq.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.traq.core.common.model.ExportFormat
import com.traq.core.common.model.MapRendererType
import com.traq.core.common.model.TrackingAccuracy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override val mapRenderer: Flow<MapRendererType> = dataStore.data.map { prefs ->
        MapRendererType.valueOf(prefs[MAP_RENDERER_KEY] ?: MapRendererType.GOOGLE.name)
    }

    override val trackingAccuracy: Flow<TrackingAccuracy> = dataStore.data.map { prefs ->
        TrackingAccuracy.valueOf(prefs[TRACKING_ACCURACY_KEY] ?: TrackingAccuracy.HIGH.name)
    }

    override val aiEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[AI_ENABLED_KEY] ?: true
    }

    override val autoExportEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[AUTO_EXPORT_KEY] ?: false
    }

    override val defaultExportFormat: Flow<ExportFormat> = dataStore.data.map { prefs ->
        ExportFormat.valueOf(prefs[DEFAULT_EXPORT_FORMAT_KEY] ?: ExportFormat.TRAQ.name)
    }

    override val oemGuideCompleted: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[OEM_GUIDE_COMPLETED_KEY] ?: false
    }

    override suspend fun setMapRenderer(type: MapRendererType) {
        dataStore.edit { it[MAP_RENDERER_KEY] = type.name }
    }

    override suspend fun setTrackingAccuracy(accuracy: TrackingAccuracy) {
        dataStore.edit { it[TRACKING_ACCURACY_KEY] = accuracy.name }
    }

    override suspend fun setAiEnabled(enabled: Boolean) {
        dataStore.edit { it[AI_ENABLED_KEY] = enabled }
    }

    override suspend fun setAutoExportEnabled(enabled: Boolean) {
        dataStore.edit { it[AUTO_EXPORT_KEY] = enabled }
    }

    override suspend fun setDefaultExportFormat(format: ExportFormat) {
        dataStore.edit { it[DEFAULT_EXPORT_FORMAT_KEY] = format.name }
    }

    override suspend fun setOemGuideCompleted(completed: Boolean) {
        dataStore.edit { it[OEM_GUIDE_COMPLETED_KEY] = completed }
    }

    companion object {
        val MAP_RENDERER_KEY = stringPreferencesKey("map_renderer")
        val TRACKING_ACCURACY_KEY = stringPreferencesKey("tracking_accuracy")
        val AI_ENABLED_KEY = booleanPreferencesKey("ai_enabled")
        val AUTO_EXPORT_KEY = booleanPreferencesKey("auto_export")
        val DEFAULT_EXPORT_FORMAT_KEY = stringPreferencesKey("default_export_format")
        val OEM_GUIDE_COMPLETED_KEY = booleanPreferencesKey("oem_guide_completed")
    }
}
