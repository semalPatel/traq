package com.traq.core.maps.maplibre;

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
public final class MapLibreRenderer_Factory implements Factory<MapLibreRenderer> {
  private final Provider<Context> contextProvider;

  public MapLibreRenderer_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MapLibreRenderer get() {
    return newInstance(contextProvider.get());
  }

  public static MapLibreRenderer_Factory create(Provider<Context> contextProvider) {
    return new MapLibreRenderer_Factory(contextProvider);
  }

  public static MapLibreRenderer newInstance(Context context) {
    return new MapLibreRenderer(context);
  }
}
