package com.traq.core.maps.di;

import com.traq.core.data.repository.UserPreferencesRepository;
import com.traq.core.maps.api.MapRenderer;
import com.traq.core.maps.google.GoogleMapsRenderer;
import com.traq.core.maps.maplibre.MapLibreRenderer;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class MapModule_ProvideMapRendererFactory implements Factory<MapRenderer> {
  private final Provider<UserPreferencesRepository> prefsProvider;

  private final Provider<GoogleMapsRenderer> googleRendererProvider;

  private final Provider<MapLibreRenderer> mapLibreRendererProvider;

  public MapModule_ProvideMapRendererFactory(Provider<UserPreferencesRepository> prefsProvider,
      Provider<GoogleMapsRenderer> googleRendererProvider,
      Provider<MapLibreRenderer> mapLibreRendererProvider) {
    this.prefsProvider = prefsProvider;
    this.googleRendererProvider = googleRendererProvider;
    this.mapLibreRendererProvider = mapLibreRendererProvider;
  }

  @Override
  public MapRenderer get() {
    return provideMapRenderer(prefsProvider.get(), googleRendererProvider.get(), mapLibreRendererProvider.get());
  }

  public static MapModule_ProvideMapRendererFactory create(
      Provider<UserPreferencesRepository> prefsProvider,
      Provider<GoogleMapsRenderer> googleRendererProvider,
      Provider<MapLibreRenderer> mapLibreRendererProvider) {
    return new MapModule_ProvideMapRendererFactory(prefsProvider, googleRendererProvider, mapLibreRendererProvider);
  }

  public static MapRenderer provideMapRenderer(UserPreferencesRepository prefs,
      GoogleMapsRenderer googleRenderer, MapLibreRenderer mapLibreRenderer) {
    return Preconditions.checkNotNullFromProvides(MapModule.INSTANCE.provideMapRenderer(prefs, googleRenderer, mapLibreRenderer));
  }
}
