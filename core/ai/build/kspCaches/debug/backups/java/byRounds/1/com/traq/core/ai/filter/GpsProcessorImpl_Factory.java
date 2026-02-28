package com.traq.core.ai.filter;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class GpsProcessorImpl_Factory implements Factory<GpsProcessorImpl> {
  @Override
  public GpsProcessorImpl get() {
    return newInstance();
  }

  public static GpsProcessorImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GpsProcessorImpl newInstance() {
    return new GpsProcessorImpl();
  }

  private static final class InstanceHolder {
    private static final GpsProcessorImpl_Factory INSTANCE = new GpsProcessorImpl_Factory();
  }
}
