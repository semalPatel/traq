package com.traq.core.location.di;

import com.traq.core.location.controller.TrackingController;
import com.traq.core.location.controller.TrackingControllerImpl;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class LocationModule_ProvideTrackingControllerFactory implements Factory<TrackingController> {
  private final Provider<TrackingControllerImpl> implProvider;

  public LocationModule_ProvideTrackingControllerFactory(
      Provider<TrackingControllerImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public TrackingController get() {
    return provideTrackingController(implProvider.get());
  }

  public static LocationModule_ProvideTrackingControllerFactory create(
      Provider<TrackingControllerImpl> implProvider) {
    return new LocationModule_ProvideTrackingControllerFactory(implProvider);
  }

  public static TrackingController provideTrackingController(TrackingControllerImpl impl) {
    return Preconditions.checkNotNullFromProvides(LocationModule.INSTANCE.provideTrackingController(impl));
  }
}
