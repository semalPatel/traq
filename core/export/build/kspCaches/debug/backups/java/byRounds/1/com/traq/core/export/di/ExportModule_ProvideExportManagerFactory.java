package com.traq.core.export.di;

import com.traq.core.export.api.ExportManager;
import com.traq.core.export.manager.ExportManagerImpl;
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
public final class ExportModule_ProvideExportManagerFactory implements Factory<ExportManager> {
  private final Provider<ExportManagerImpl> implProvider;

  public ExportModule_ProvideExportManagerFactory(Provider<ExportManagerImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public ExportManager get() {
    return provideExportManager(implProvider.get());
  }

  public static ExportModule_ProvideExportManagerFactory create(
      Provider<ExportManagerImpl> implProvider) {
    return new ExportModule_ProvideExportManagerFactory(implProvider);
  }

  public static ExportManager provideExportManager(ExportManagerImpl impl) {
    return Preconditions.checkNotNullFromProvides(ExportModule.INSTANCE.provideExportManager(impl));
  }
}
