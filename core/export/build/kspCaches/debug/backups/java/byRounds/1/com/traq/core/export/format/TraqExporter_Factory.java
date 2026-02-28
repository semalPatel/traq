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
public final class TraqExporter_Factory implements Factory<TraqExporter> {
  @Override
  public TraqExporter get() {
    return newInstance();
  }

  public static TraqExporter_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TraqExporter newInstance() {
    return new TraqExporter();
  }

  private static final class InstanceHolder {
    private static final TraqExporter_Factory INSTANCE = new TraqExporter_Factory();
  }
}
