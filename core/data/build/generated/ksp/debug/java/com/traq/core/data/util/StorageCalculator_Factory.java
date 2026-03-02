package com.traq.core.data.util;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class StorageCalculator_Factory implements Factory<StorageCalculator> {
  private final Provider<Context> contextProvider;

  public StorageCalculator_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public StorageCalculator get() {
    return newInstance(contextProvider.get());
  }

  public static StorageCalculator_Factory create(Provider<Context> contextProvider) {
    return new StorageCalculator_Factory(contextProvider);
  }

  public static StorageCalculator newInstance(Context context) {
    return new StorageCalculator(context);
  }
}
