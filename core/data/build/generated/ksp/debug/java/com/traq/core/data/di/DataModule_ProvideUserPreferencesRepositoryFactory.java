package com.traq.core.data.di;

import com.traq.core.data.repository.UserPreferencesRepository;
import com.traq.core.data.repository.UserPreferencesRepositoryImpl;
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
public final class DataModule_ProvideUserPreferencesRepositoryFactory implements Factory<UserPreferencesRepository> {
  private final Provider<UserPreferencesRepositoryImpl> implProvider;

  public DataModule_ProvideUserPreferencesRepositoryFactory(
      Provider<UserPreferencesRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public UserPreferencesRepository get() {
    return provideUserPreferencesRepository(implProvider.get());
  }

  public static DataModule_ProvideUserPreferencesRepositoryFactory create(
      Provider<UserPreferencesRepositoryImpl> implProvider) {
    return new DataModule_ProvideUserPreferencesRepositoryFactory(implProvider);
  }

  public static UserPreferencesRepository provideUserPreferencesRepository(
      UserPreferencesRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideUserPreferencesRepository(impl));
  }
}
