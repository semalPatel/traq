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
public final class KmlExporter_Factory implements Factory<KmlExporter> {
  @Override
  public KmlExporter get() {
    return newInstance();
  }

  public static KmlExporter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static KmlExporter newInstance() {
    return new KmlExporter();
  }

  private static final class InstanceHolder {
    private static final KmlExporter_Factory INSTANCE = new KmlExporter_Factory();
  }
}
