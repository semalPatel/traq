package com.traq.core.permissions;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class BatteryOptimizationHelper_Factory implements Factory<BatteryOptimizationHelper> {
  private final Provider<Context> contextProvider;

  public BatteryOptimizationHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public BatteryOptimizationHelper get() {
    return newInstance(contextProvider.get());
  }

  public static BatteryOptimizationHelper_Factory create(Provider<Context> contextProvider) {
    return new BatteryOptimizationHelper_Factory(contextProvider);
  }

  public static BatteryOptimizationHelper newInstance(Context context) {
    return new BatteryOptimizationHelper(context);
  }
}
