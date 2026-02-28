package com.traq.core.data.di;

import com.traq.core.data.repository.TrackPointRepository;
import com.traq.core.data.repository.TrackPointRepositoryImpl;
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
public final class DataModule_ProvideTrackPointRepositoryFactory implements Factory<TrackPointRepository> {
  private final Provider<TrackPointRepositoryImpl> implProvider;

  public DataModule_ProvideTrackPointRepositoryFactory(
      Provider<TrackPointRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public TrackPointRepository get() {
    return provideTrackPointRepository(implProvider.get());
  }

  public static DataModule_ProvideTrackPointRepositoryFactory create(
      Provider<TrackPointRepositoryImpl> implProvider) {
    return new DataModule_ProvideTrackPointRepositoryFactory(implProvider);
  }

  public static TrackPointRepository provideTrackPointRepository(TrackPointRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideTrackPointRepository(impl));
  }
}
