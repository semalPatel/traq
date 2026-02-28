package com.traq.core.ai.sampling;

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
public final class RuleBasedSampler_Factory implements Factory<RuleBasedSampler> {
  @Override
  public RuleBasedSampler get() {
    return newInstance();
  }

  public static RuleBasedSampler_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RuleBasedSampler newInstance() {
    return new RuleBasedSampler();
  }

  private static final class InstanceHolder {
    private static final RuleBasedSampler_Factory INSTANCE = new RuleBasedSampler_Factory();
  }
}
