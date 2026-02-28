package com.traq.core.maps.google;

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
public final class GoogleMapsRenderer_Factory implements Factory<GoogleMapsRenderer> {
  @Override
  public GoogleMapsRenderer get() {
    return newInstance();
  }

  public static GoogleMapsRenderer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GoogleMapsRenderer newInstance() {
    return new GoogleMapsRenderer();
  }

  private static final class InstanceHolder {
    private static final GoogleMapsRenderer_Factory INSTANCE = new GoogleMapsRenderer_Factory();
  }
}
