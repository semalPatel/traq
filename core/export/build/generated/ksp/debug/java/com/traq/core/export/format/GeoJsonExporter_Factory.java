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
public final class GeoJsonExporter_Factory implements Factory<GeoJsonExporter> {
  @Override
  public GeoJsonExporter get() {
    return newInstance();
  }

  public static GeoJsonExporter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GeoJsonExporter newInstance() {
    return new GeoJsonExporter();
  }

  private static final class InstanceHolder {
    private static final GeoJsonExporter_Factory INSTANCE = new GeoJsonExporter_Factory();
  }
}
