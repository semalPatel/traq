package com.traq.core.location.util;

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
public final class WakeLockManager_Factory implements Factory<WakeLockManager> {
  private final Provider<Context> contextProvider;

  public WakeLockManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public WakeLockManager get() {
    return newInstance(contextProvider.get());
  }

  public static WakeLockManager_Factory create(Provider<Context> contextProvider) {
    return new WakeLockManager_Factory(contextProvider);
  }

  public static WakeLockManager newInstance(Context context) {
    return new WakeLockManager(context);
  }
}
