package com.traq.core.sensors.di;

import com.traq.core.sensors.collector.SensorCollector;
import com.traq.core.sensors.collector.SensorCollectorImpl;
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
public final class SensorModule_ProvideSensorCollectorFactory implements Factory<SensorCollector> {
  private final Provider<SensorCollectorImpl> implProvider;

  public SensorModule_ProvideSensorCollectorFactory(Provider<SensorCollectorImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public SensorCollector get() {
    return provideSensorCollector(implProvider.get());
  }

  public static SensorModule_ProvideSensorCollectorFactory create(
      Provider<SensorCollectorImpl> implProvider) {
    return new SensorModule_ProvideSensorCollectorFactory(implProvider);
  }

  public static SensorCollector provideSensorCollector(SensorCollectorImpl impl) {
    return Preconditions.checkNotNullFromProvides(SensorModule.INSTANCE.provideSensorCollector(impl));
  }
}
