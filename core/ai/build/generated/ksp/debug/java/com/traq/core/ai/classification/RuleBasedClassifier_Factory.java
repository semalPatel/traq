package com.traq.core.ai.classification;

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
public final class RuleBasedClassifier_Factory implements Factory<RuleBasedClassifier> {
  @Override
  public RuleBasedClassifier get() {
    return newInstance();
  }

  public static RuleBasedClassifier_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RuleBasedClassifier newInstance() {
    return new RuleBasedClassifier();
  }

  private static final class InstanceHolder {
    private static final RuleBasedClassifier_Factory INSTANCE = new RuleBasedClassifier_Factory();
  }
}
