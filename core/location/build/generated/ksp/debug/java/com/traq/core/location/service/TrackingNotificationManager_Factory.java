package com.traq.core.location.service;

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
public final class TrackingNotificationManager_Factory implements Factory<TrackingNotificationManager> {
  private final Provider<Context> contextProvider;

  public TrackingNotificationManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TrackingNotificationManager get() {
    return newInstance(contextProvider.get());
  }

  public static TrackingNotificationManager_Factory create(Provider<Context> contextProvider) {
    return new TrackingNotificationManager_Factory(contextProvider);
  }

  public static TrackingNotificationManager newInstance(Context context) {
    return new TrackingNotificationManager(context);
  }
}
