package com.traq.core.export.di

import com.traq.core.common.model.ExportFormat
import com.traq.core.export.api.ExportManager
import com.traq.core.export.api.TripExporter
import com.traq.core.export.format.GeoJsonExporter
import com.traq.core.export.format.GpxExporter
import com.traq.core.export.format.KmlExporter
import com.traq.core.export.format.TraqExporter
import com.traq.core.export.manager.ExportManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExportModule {

    @Provides
    fun provideExporters(
        traqExporter: TraqExporter,
        gpxExporter: GpxExporter,
        geoJsonExporter: GeoJsonExporter,
        kmlExporter: KmlExporter
    ): Map<ExportFormat, TripExporter> = mapOf(
        ExportFormat.TRAQ to traqExporter,
        ExportFormat.GPX to gpxExporter,
        ExportFormat.GEOJSON to geoJsonExporter,
        ExportFormat.KML to kmlExporter
    )

    @Provides
    @Singleton
    fun provideExportManager(impl: ExportManagerImpl): ExportManager = impl
}
