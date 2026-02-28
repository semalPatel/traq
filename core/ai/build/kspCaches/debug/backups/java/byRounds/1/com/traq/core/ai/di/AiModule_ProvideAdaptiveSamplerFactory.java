package com.traq.core.ai.di;

import com.traq.core.ai.sampling.AdaptiveSampler;
import com.traq.core.ai.sampling.RuleBasedSampler;
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
public final class AiModule_ProvideAdaptiveSamplerFactory implements Factory<AdaptiveSampler> {
  private final Provider<RuleBasedSampler> implProvider;

  public AiModule_ProvideAdaptiveSamplerFactory(Provider<RuleBasedSampler> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public AdaptiveSampler get() {
    return provideAdaptiveSampler(implProvider.get());
  }

  public static AiModule_ProvideAdaptiveSamplerFactory create(
      Provider<RuleBasedSampler> implProvider) {
    return new AiModule_ProvideAdaptiveSamplerFactory(implProvider);
  }

  public static AdaptiveSampler provideAdaptiveSampler(RuleBasedSampler impl) {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideAdaptiveSampler(impl));
  }
}
