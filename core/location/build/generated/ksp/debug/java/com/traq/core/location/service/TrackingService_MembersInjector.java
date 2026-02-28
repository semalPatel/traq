package com.traq.core.location.service;

import com.traq.core.data.repository.TrackPointRepository;
import com.traq.core.data.repository.TripRepository;
import com.traq.core.location.provider.LocationProvider;
import com.traq.core.location.util.BatteryMonitor;
import com.traq.core.location.util.WakeLockManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class TrackingService_MembersInjector implements MembersInjector<TrackingService> {
  private final Provider<LocationProvider> locationProvider;

  private final Provider<TrackPointRepository> trackPointRepositoryProvider;

  private final Provider<TripRepository> tripRepositoryProvider;

  private final Provider<TrackingNotificationManager> notificationManagerProvider;

  private final Provider<WakeLockManager> wakeLockManagerProvider;

  private final Provider<BatteryMonitor> batteryMonitorProvider;

  public TrackingService_MembersInjector(Provider<LocationProvider> locationProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackingNotificationManager> notificationManagerProvider,
      Provider<WakeLockManager> wakeLockManagerProvider,
      Provider<BatteryMonitor> batteryMonitorProvider) {
    this.locationProvider = locationProvider;
    this.trackPointRepositoryProvider = trackPointRepositoryProvider;
    this.tripRepositoryProvider = tripRepositoryProvider;
    this.notificationManagerProvider = notificationManagerProvider;
    this.wakeLockManagerProvider = wakeLockManagerProvider;
    this.batteryMonitorProvider = batteryMonitorProvider;
  }

  public static MembersInjector<TrackingService> create(Provider<LocationProvider> locationProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackingNotificationManager> notificationManagerProvider,
      Provider<WakeLockManager> wakeLockManagerProvider,
      Provider<BatteryMonitor> batteryMonitorProvider) {
    return new TrackingService_MembersInjector(locationProvider, trackPointRepositoryProvider, tripRepositoryProvider, notificationManagerProvider, wakeLockManagerProvider, batteryMonitorProvider);
  }

  @Override
  public void injectMembers(TrackingService instance) {
    injectLocationProvider(instance, locationProvider.get());
    injectTrackPointRepository(instance, trackPointRepositoryProvider.get());
    injectTripRepository(instance, tripRepositoryProvider.get());
    injectNotificationManager(instance, notificationManagerProvider.get());
    injectWakeLockManager(instance, wakeLockManagerProvider.get());
    injectBatteryMonitor(instance, batteryMonitorProvider.get());
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.locationProvider")
  public static void injectLocationProvider(TrackingService instance,
      LocationProvider locationProvider) {
    instance.locationProvider = locationProvider;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.trackPointRepository")
  public static void injectTrackPointRepository(TrackingService instance,
      TrackPointRepository trackPointRepository) {
    instance.trackPointRepository = trackPointRepository;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.tripRepository")
  public static void injectTripRepository(TrackingService instance, TripRepository tripRepository) {
    instance.tripRepository = tripRepository;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.notificationManager")
  public static void injectNotificationManager(TrackingService instance,
      TrackingNotificationManager notificationManager) {
    instance.notificationManager = notificationManager;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.wakeLockManager")
  public static void injectWakeLockManager(TrackingService instance,
      WakeLockManager wakeLockManager) {
    instance.wakeLockManager = wakeLockManager;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.batteryMonitor")
  public static void injectBatteryMonitor(TrackingService instance, BatteryMonitor batteryMonitor) {
    instance.batteryMonitor = batteryMonitor;
  }
}
