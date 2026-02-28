package com.traq.feature.tripdetail.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import com.traq.core.data.repository.TrackPointRepository;
import com.traq.core.data.repository.TripRepository;
import com.traq.core.export.api.ExportManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class TripDetailViewModel_Factory implements Factory<TripDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<TripRepository> tripRepositoryProvider;

  private final Provider<TrackPointRepository> trackPointRepositoryProvider;

  private final Provider<ExportManager> exportManagerProvider;

  public TripDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider,
      Provider<ExportManager> exportManagerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.tripRepositoryProvider = tripRepositoryProvider;
    this.trackPointRepositoryProvider = trackPointRepositoryProvider;
    this.exportManagerProvider = exportManagerProvider;
  }

  @Override
  public TripDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), tripRepositoryProvider.get(), trackPointRepositoryProvider.get(), exportManagerProvider.get());
  }

  public static TripDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider,
      Provider<ExportManager> exportManagerProvider) {
    return new TripDetailViewModel_Factory(savedStateHandleProvider, tripRepositoryProvider, trackPointRepositoryProvider, exportManagerProvider);
  }

  public static TripDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      TripRepository tripRepository, TrackPointRepository trackPointRepository,
      ExportManager exportManager) {
    return new TripDetailViewModel(savedStateHandle, tripRepository, trackPointRepository, exportManager);
  }
}
