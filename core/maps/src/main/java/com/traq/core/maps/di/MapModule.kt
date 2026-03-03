package com.traq.core.maps.di

import com.traq.core.maps.api.MapRenderer
import com.traq.core.maps.maplibre.MapLibreRenderer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapModule {
    @Binds
    @Singleton
    abstract fun bindMapRenderer(impl: MapLibreRenderer): MapRenderer
}
