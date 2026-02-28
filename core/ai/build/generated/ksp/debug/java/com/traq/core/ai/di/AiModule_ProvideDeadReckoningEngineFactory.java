package com.traq.core.ai.di;

import com.traq.core.ai.deadreckoning.DeadReckoningEngine;
import com.traq.core.ai.deadreckoning.DeadReckoningEngineImpl;
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
public final class AiModule_ProvideDeadReckoningEngineFactory implements Factory<DeadReckoningEngine> {
  private final Provider<DeadReckoningEngineImpl> implProvider;

  public AiModule_ProvideDeadReckoningEngineFactory(
      Provider<DeadReckoningEngineImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public DeadReckoningEngine get() {
    return provideDeadReckoningEngine(implProvider.get());
  }

  public static AiModule_ProvideDeadReckoningEngineFactory create(
      Provider<DeadReckoningEngineImpl> implProvider) {
    return new AiModule_ProvideDeadReckoningEngineFactory(implProvider);
  }

  public static DeadReckoningEngine provideDeadReckoningEngine(DeadReckoningEngineImpl impl) {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideDeadReckoningEngine(impl));
  }
}
