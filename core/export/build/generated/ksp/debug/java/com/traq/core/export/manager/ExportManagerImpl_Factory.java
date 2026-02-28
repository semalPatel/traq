package com.traq.core.export.manager;

import android.content.Context;
import com.traq.core.common.model.ExportFormat;
import com.traq.core.data.repository.TrackPointRepository;
import com.traq.core.data.repository.TripRepository;
import com.traq.core.export.api.TripExporter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import java.util.Map;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ExportManagerImpl_Factory implements Factory<ExportManagerImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<TripRepository> tripRepositoryProvider;

  private final Provider<TrackPointRepository> trackPointRepositoryProvider;

  private final Provider<Map<ExportFormat, TripExporter>> exportersProvider;

  public ExportManagerImpl_Factory(Provider<Context> contextProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider,
      Provider<Map<ExportFormat, TripExporter>> exportersProvider) {
    this.contextProvider = contextProvider;
    this.tripRepositoryProvider = tripRepositoryProvider;
    this.trackPointRepositoryProvider = trackPointRepositoryProvider;
    this.exportersProvider = exportersProvider;
  }

  @Override
  public ExportManagerImpl get() {
    return newInstance(contextProvider.get(), tripRepositoryProvider.get(), trackPointRepositoryProvider.get(), exportersProvider.get());
  }

  public static ExportManagerImpl_Factory create(Provider<Context> contextProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider,
      Provider<Map<ExportFormat, TripExporter>> exportersProvider) {
    return new ExportManagerImpl_Factory(contextProvider, tripRepositoryProvider, trackPointRepositoryProvider, exportersProvider);
  }

  public static ExportManagerImpl newInstance(Context context, TripRepository tripRepository,
      TrackPointRepository trackPointRepository, Map<ExportFormat, TripExporter> exporters) {
    return new ExportManagerImpl(context, tripRepository, trackPointRepository, exporters);
  }
}
