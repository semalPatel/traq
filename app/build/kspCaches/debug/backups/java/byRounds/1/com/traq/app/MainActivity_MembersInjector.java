package com.traq.app;

import com.traq.core.maps.api.MapRenderer;
import com.traq.core.permissions.PermissionManager;
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

  private final Provider<PermissionManager> permissionManagerProvider;

  public MainActivity_MembersInjector(Provider<MapRenderer> mapRendererProvider,
      Provider<PermissionManager> permissionManagerProvider) {
    this.mapRendererProvider = mapRendererProvider;
    this.permissionManagerProvider = permissionManagerProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<MapRenderer> mapRendererProvider,
      Provider<PermissionManager> permissionManagerProvider) {
    return new MainActivity_MembersInjector(mapRendererProvider, permissionManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectMapRenderer(instance, mapRendererProvider.get());
    injectPermissionManager(instance, permissionManagerProvider.get());
  }

  @InjectedFieldSignature("com.traq.app.MainActivity.mapRenderer")
  public static void injectMapRenderer(MainActivity instance, MapRenderer mapRenderer) {
    instance.mapRenderer = mapRenderer;
  }

  @InjectedFieldSignature("com.traq.app.MainActivity.permissionManager")
  public static void injectPermissionManager(MainActivity instance,
      PermissionManager permissionManager) {
    instance.permissionManager = permissionManager;
  }
}
