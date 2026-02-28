package com.traq.core.ai.deadreckoning;

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
public final class DeadReckoningEngineImpl_Factory implements Factory<DeadReckoningEngineImpl> {
  @Override
  public DeadReckoningEngineImpl get() {
    return newInstance();
  }

  public static DeadReckoningEngineImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DeadReckoningEngineImpl newInstance() {
    return new DeadReckoningEngineImpl();
  }

  private static final class InstanceHolder {
    private static final DeadReckoningEngineImpl_Factory INSTANCE = new DeadReckoningEngineImpl_Factory();
  }
}
