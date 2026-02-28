package com.traq.core.ai.di;

import com.traq.core.ai.filter.GpsProcessor;
import com.traq.core.ai.filter.GpsProcessorImpl;
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
public final class AiModule_ProvideGpsProcessorFactory implements Factory<GpsProcessor> {
  private final Provider<GpsProcessorImpl> implProvider;

  public AiModule_ProvideGpsProcessorFactory(Provider<GpsProcessorImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public GpsProcessor get() {
    return provideGpsProcessor(implProvider.get());
  }

  public static AiModule_ProvideGpsProcessorFactory create(
      Provider<GpsProcessorImpl> implProvider) {
    return new AiModule_ProvideGpsProcessorFactory(implProvider);
  }

  public static GpsProcessor provideGpsProcessor(GpsProcessorImpl impl) {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideGpsProcessor(impl));
  }
}
