package com.traq.core.export.format;

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
public final class GpxExporter_Factory implements Factory<GpxExporter> {
  @Override
  public GpxExporter get() {
    return newInstance();
  }

  public static GpxExporter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GpxExporter newInstance() {
    return new GpxExporter();
  }

  private static final class InstanceHolder {
    private static final GpxExporter_Factory INSTANCE = new GpxExporter_Factory();
  }
}
