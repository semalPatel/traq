package com.traq.core.ai.di;

import com.traq.core.ai.classification.RuleBasedClassifier;
import com.traq.core.ai.classification.TransportClassifier;
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
public final class AiModule_ProvideTransportClassifierFactory implements Factory<TransportClassifier> {
  private final Provider<RuleBasedClassifier> implProvider;

  public AiModule_ProvideTransportClassifierFactory(Provider<RuleBasedClassifier> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public TransportClassifier get() {
    return provideTransportClassifier(implProvider.get());
  }

  public static AiModule_ProvideTransportClassifierFactory create(
      Provider<RuleBasedClassifier> implProvider) {
    return new AiModule_ProvideTransportClassifierFactory(implProvider);
  }

  public static TransportClassifier provideTransportClassifier(RuleBasedClassifier impl) {
    return Preconditions.checkNotNullFromProvides(AiModule.INSTANCE.provideTransportClassifier(impl));
  }
}
