package com.traq.feature.tracking.viewmodel;

import androidx.lifecycle.SavedStateHandle;
import com.traq.core.data.repository.TrackPointRepository;
import com.traq.core.location.controller.TrackingController;
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
public final class TrackingViewModel_Factory implements Factory<TrackingViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<TrackingController> trackingControllerProvider;

  private final Provider<TrackPointRepository> trackPointRepositoryProvider;

  public TrackingViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TrackingController> trackingControllerProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.trackingControllerProvider = trackingControllerProvider;
    this.trackPointRepositoryProvider = trackPointRepositoryProvider;
  }

  @Override
  public TrackingViewModel get() {
    return newInstance(savedStateHandleProvider.get(), trackingControllerProvider.get(), trackPointRepositoryProvider.get());
  }

  public static TrackingViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TrackingController> trackingControllerProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider) {
    return new TrackingViewModel_Factory(savedStateHandleProvider, trackingControllerProvider, trackPointRepositoryProvider);
  }

  public static TrackingViewModel newInstance(SavedStateHandle savedStateHandle,
      TrackingController trackingController, TrackPointRepository trackPointRepository) {
    return new TrackingViewModel(savedStateHandle, trackingController, trackPointRepository);
  }
}
