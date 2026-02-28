package com.traq.feature.dashboard.viewmodel;

import com.traq.core.data.repository.TripRepository;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<TripRepository> tripRepositoryProvider;

  private final Provider<TrackingController> trackingControllerProvider;

  public DashboardViewModel_Factory(Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackingController> trackingControllerProvider) {
    this.tripRepositoryProvider = tripRepositoryProvider;
    this.trackingControllerProvider = trackingControllerProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(tripRepositoryProvider.get(), trackingControllerProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackingController> trackingControllerProvider) {
    return new DashboardViewModel_Factory(tripRepositoryProvider, trackingControllerProvider);
  }

  public static DashboardViewModel newInstance(TripRepository tripRepository,
      TrackingController trackingController) {
    return new DashboardViewModel(tripRepository, trackingController);
  }
}
