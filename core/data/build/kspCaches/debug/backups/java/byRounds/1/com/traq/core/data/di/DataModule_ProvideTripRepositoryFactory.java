package com.traq.core.data.di;

import com.traq.core.data.repository.TripRepository;
import com.traq.core.data.repository.TripRepositoryImpl;
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
public final class DataModule_ProvideTripRepositoryFactory implements Factory<TripRepository> {
  private final Provider<TripRepositoryImpl> implProvider;

  public DataModule_ProvideTripRepositoryFactory(Provider<TripRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public TripRepository get() {
    return provideTripRepository(implProvider.get());
  }

  public static DataModule_ProvideTripRepositoryFactory create(
      Provider<TripRepositoryImpl> implProvider) {
    return new DataModule_ProvideTripRepositoryFactory(implProvider);
  }

  public static TripRepository provideTripRepository(TripRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideTripRepository(impl));
  }
}
