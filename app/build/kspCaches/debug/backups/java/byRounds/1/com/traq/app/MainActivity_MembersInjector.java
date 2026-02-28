package com.traq.app;

import com.traq.core.maps.api.MapRenderer;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<MapRenderer> mapRendererProvider;

  public MainActivity_MembersInjector(Provider<MapRenderer> mapRendererProvider) {
    this.mapRendererProvider = mapRendererProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<MapRenderer> mapRendererProvider) {
    return new MainActivity_MembersInjector(mapRendererProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectMapRenderer(instance, mapRendererProvider.get());
  }

  @InjectedFieldSignature("com.traq.app.MainActivity.mapRenderer")
  public static void injectMapRenderer(MainActivity instance, MapRenderer mapRenderer) {
    instance.mapRenderer = mapRenderer;
  }
}
