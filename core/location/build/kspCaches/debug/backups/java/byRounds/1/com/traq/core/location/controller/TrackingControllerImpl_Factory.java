package com.traq.core.location.controller;

import android.content.Context;
import com.traq.core.data.repository.TripRepository;
import com.traq.core.location.util.BatteryMonitor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class TrackingControllerImpl_Factory implements Factory<TrackingControllerImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<TripRepository> tripRepositoryProvider;

  private final Provider<BatteryMonitor> batteryMonitorProvider;

  public TrackingControllerImpl_Factory(Provider<Context> contextProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<BatteryMonitor> batteryMonitorProvider) {
    this.contextProvider = contextProvider;
    this.tripRepositoryProvider = tripRepositoryProvider;
    this.batteryMonitorProvider = batteryMonitorProvider;
  }

  @Override
  public TrackingControllerImpl get() {
    return newInstance(contextProvider.get(), tripRepositoryProvider.get(), batteryMonitorProvider.get());
  }

  public static TrackingControllerImpl_Factory create(Provider<Context> contextProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<BatteryMonitor> batteryMonitorProvider) {
    return new TrackingControllerImpl_Factory(contextProvider, tripRepositoryProvider, batteryMonitorProvider);
  }

  public static TrackingControllerImpl newInstance(Context context, TripRepository tripRepository,
      BatteryMonitor batteryMonitor) {
    return new TrackingControllerImpl(context, tripRepository, batteryMonitor);
  }
}
