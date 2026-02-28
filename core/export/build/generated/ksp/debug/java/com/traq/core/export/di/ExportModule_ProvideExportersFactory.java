package com.traq.core.export.di;

import com.traq.core.common.model.ExportFormat;
import com.traq.core.export.api.TripExporter;
import com.traq.core.export.format.GeoJsonExporter;
import com.traq.core.export.format.GpxExporter;
import com.traq.core.export.format.KmlExporter;
import com.traq.core.export.format.TraqExporter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Map;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ExportModule_ProvideExportersFactory implements Factory<Map<ExportFormat, TripExporter>> {
  private final Provider<TraqExporter> traqExporterProvider;

  private final Provider<GpxExporter> gpxExporterProvider;

  private final Provider<GeoJsonExporter> geoJsonExporterProvider;

  private final Provider<KmlExporter> kmlExporterProvider;

  public ExportModule_ProvideExportersFactory(Provider<TraqExporter> traqExporterProvider,
      Provider<GpxExporter> gpxExporterProvider, Provider<GeoJsonExporter> geoJsonExporterProvider,
      Provider<KmlExporter> kmlExporterProvider) {
    this.traqExporterProvider = traqExporterProvider;
    this.gpxExporterProvider = gpxExporterProvider;
    this.geoJsonExporterProvider = geoJsonExporterProvider;
    this.kmlExporterProvider = kmlExporterProvider;
  }

  @Override
  public Map<ExportFormat, TripExporter> get() {
    return provideExporters(traqExporterProvider.get(), gpxExporterProvider.get(), geoJsonExporterProvider.get(), kmlExporterProvider.get());
  }

  public static ExportModule_ProvideExportersFactory create(
      Provider<TraqExporter> traqExporterProvider, Provider<GpxExporter> gpxExporterProvider,
      Provider<GeoJsonExporter> geoJsonExporterProvider,
      Provider<KmlExporter> kmlExporterProvider) {
    return new ExportModule_ProvideExportersFactory(traqExporterProvider, gpxExporterProvider, geoJsonExporterProvider, kmlExporterProvider);
  }

  public static Map<ExportFormat, TripExporter> provideExporters(TraqExporter traqExporter,
      GpxExporter gpxExporter, GeoJsonExporter geoJsonExporter, KmlExporter kmlExporter) {
    return Preconditions.checkNotNullFromProvides(ExportModule.INSTANCE.provideExporters(traqExporter, gpxExporter, geoJsonExporter, kmlExporter));
  }
}
