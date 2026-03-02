package com.traq.core.location.service;

import com.traq.core.ai.classification.TransportClassifier;
import com.traq.core.ai.filter.GpsProcessor;
import com.traq.core.ai.lifecycle.TripLifecycleManager;
import com.traq.core.ai.sampling.AdaptiveSampler;
import com.traq.core.data.repository.TrackPointRepository;
import com.traq.core.data.repository.TripRepository;
import com.traq.core.data.repository.UserPreferencesRepository;
import com.traq.core.location.provider.LocationProvider;
import com.traq.core.location.util.BatteryMonitor;
import com.traq.core.location.util.WakeLockManager;
import com.traq.core.sensors.collector.SensorCollector;
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

  private final Provider<SensorCollector> sensorCollectorProvider;

  private final Provider<GpsProcessor> gpsProcessorProvider;

  private final Provider<AdaptiveSampler> adaptiveSamplerProvider;

  private final Provider<TransportClassifier> transportClassifierProvider;

  private final Provider<TripLifecycleManager> tripLifecycleManagerProvider;

  private final Provider<UserPreferencesRepository> prefsRepositoryProvider;

  public TrackingService_MembersInjector(Provider<LocationProvider> locationProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackingNotificationManager> notificationManagerProvider,
      Provider<WakeLockManager> wakeLockManagerProvider,
      Provider<BatteryMonitor> batteryMonitorProvider,
      Provider<SensorCollector> sensorCollectorProvider,
      Provider<GpsProcessor> gpsProcessorProvider,
      Provider<AdaptiveSampler> adaptiveSamplerProvider,
      Provider<TransportClassifier> transportClassifierProvider,
      Provider<TripLifecycleManager> tripLifecycleManagerProvider,
      Provider<UserPreferencesRepository> prefsRepositoryProvider) {
    this.locationProvider = locationProvider;
    this.trackPointRepositoryProvider = trackPointRepositoryProvider;
    this.tripRepositoryProvider = tripRepositoryProvider;
    this.notificationManagerProvider = notificationManagerProvider;
    this.wakeLockManagerProvider = wakeLockManagerProvider;
    this.batteryMonitorProvider = batteryMonitorProvider;
    this.sensorCollectorProvider = sensorCollectorProvider;
    this.gpsProcessorProvider = gpsProcessorProvider;
    this.adaptiveSamplerProvider = adaptiveSamplerProvider;
    this.transportClassifierProvider = transportClassifierProvider;
    this.tripLifecycleManagerProvider = tripLifecycleManagerProvider;
    this.prefsRepositoryProvider = prefsRepositoryProvider;
  }

  public static MembersInjector<TrackingService> create(Provider<LocationProvider> locationProvider,
      Provider<TrackPointRepository> trackPointRepositoryProvider,
      Provider<TripRepository> tripRepositoryProvider,
      Provider<TrackingNotificationManager> notificationManagerProvider,
      Provider<WakeLockManager> wakeLockManagerProvider,
      Provider<BatteryMonitor> batteryMonitorProvider,
      Provider<SensorCollector> sensorCollectorProvider,
      Provider<GpsProcessor> gpsProcessorProvider,
      Provider<AdaptiveSampler> adaptiveSamplerProvider,
      Provider<TransportClassifier> transportClassifierProvider,
      Provider<TripLifecycleManager> tripLifecycleManagerProvider,
      Provider<UserPreferencesRepository> prefsRepositoryProvider) {
    return new TrackingService_MembersInjector(locationProvider, trackPointRepositoryProvider, tripRepositoryProvider, notificationManagerProvider, wakeLockManagerProvider, batteryMonitorProvider, sensorCollectorProvider, gpsProcessorProvider, adaptiveSamplerProvider, transportClassifierProvider, tripLifecycleManagerProvider, prefsRepositoryProvider);
  }

  @Override
  public void injectMembers(TrackingService instance) {
    injectLocationProvider(instance, locationProvider.get());
    injectTrackPointRepository(instance, trackPointRepositoryProvider.get());
    injectTripRepository(instance, tripRepositoryProvider.get());
    injectNotificationManager(instance, notificationManagerProvider.get());
    injectWakeLockManager(instance, wakeLockManagerProvider.get());
    injectBatteryMonitor(instance, batteryMonitorProvider.get());
    injectSensorCollector(instance, sensorCollectorProvider.get());
    injectGpsProcessor(instance, gpsProcessorProvider.get());
    injectAdaptiveSampler(instance, adaptiveSamplerProvider.get());
    injectTransportClassifier(instance, transportClassifierProvider.get());
    injectTripLifecycleManager(instance, tripLifecycleManagerProvider.get());
    injectPrefsRepository(instance, prefsRepositoryProvider.get());
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

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.sensorCollector")
  public static void injectSensorCollector(TrackingService instance,
      SensorCollector sensorCollector) {
    instance.sensorCollector = sensorCollector;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.gpsProcessor")
  public static void injectGpsProcessor(TrackingService instance, GpsProcessor gpsProcessor) {
    instance.gpsProcessor = gpsProcessor;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.adaptiveSampler")
  public static void injectAdaptiveSampler(TrackingService instance,
      AdaptiveSampler adaptiveSampler) {
    instance.adaptiveSampler = adaptiveSampler;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.transportClassifier")
  public static void injectTransportClassifier(TrackingService instance,
      TransportClassifier transportClassifier) {
    instance.transportClassifier = transportClassifier;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.tripLifecycleManager")
  public static void injectTripLifecycleManager(TrackingService instance,
      TripLifecycleManager tripLifecycleManager) {
    instance.tripLifecycleManager = tripLifecycleManager;
  }

  @InjectedFieldSignature("com.traq.core.location.service.TrackingService.prefsRepository")
  public static void injectPrefsRepository(TrackingService instance,
      UserPreferencesRepository prefsRepository) {
    instance.prefsRepository = prefsRepository;
  }
}
