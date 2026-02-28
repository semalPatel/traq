package com.traq.core.ai.lifecycle;

import com.traq.core.sensors.collector.SensorCollector;
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
public final class TripLifecycleManager_Factory implements Factory<TripLifecycleManager> {
  private final Provider<SensorCollector> sensorCollectorProvider;

  public TripLifecycleManager_Factory(Provider<SensorCollector> sensorCollectorProvider) {
    this.sensorCollectorProvider = sensorCollectorProvider;
  }

  @Override
  public TripLifecycleManager get() {
    return newInstance(sensorCollectorProvider.get());
  }

  public static TripLifecycleManager_Factory create(
      Provider<SensorCollector> sensorCollectorProvider) {
    return new TripLifecycleManager_Factory(sensorCollectorProvider);
  }

  public static TripLifecycleManager newInstance(SensorCollector sensorCollector) {
    return new TripLifecycleManager(sensorCollector);
  }
}
