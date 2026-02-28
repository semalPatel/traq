package com.traq.core.maps.di

import com.traq.core.common.model.MapRendererType
import com.traq.core.data.repository.UserPreferencesRepository
import com.traq.core.maps.api.MapRenderer
import com.traq.core.maps.google.GoogleMapsRenderer
import com.traq.core.maps.maplibre.MapLibreRenderer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {
    @Provides
    @Singleton
    fun provideMapRenderer(
        prefs: UserPreferencesRepository,
        googleRenderer: GoogleMapsRenderer,
        mapLibreRenderer: MapLibreRenderer
    ): MapRenderer {
        val type = runBlocking { prefs.mapRenderer.first() }
        return when (type) {
            MapRendererType.GOOGLE -> googleRenderer
            MapRendererType.MAPLIBRE -> mapLibreRenderer
        }
    }
}
