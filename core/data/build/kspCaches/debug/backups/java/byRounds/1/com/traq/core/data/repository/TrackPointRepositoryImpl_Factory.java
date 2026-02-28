package com.traq.core.data.repository;

import com.traq.core.data.db.dao.TrackPointDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class TrackPointRepositoryImpl_Factory implements Factory<TrackPointRepositoryImpl> {
  private final Provider<TrackPointDao> trackPointDaoProvider;

  public TrackPointRepositoryImpl_Factory(Provider<TrackPointDao> trackPointDaoProvider) {
    this.trackPointDaoProvider = trackPointDaoProvider;
  }

  @Override
  public TrackPointRepositoryImpl get() {
    return newInstance(trackPointDaoProvider.get());
  }

  public static TrackPointRepositoryImpl_Factory create(
      Provider<TrackPointDao> trackPointDaoProvider) {
    return new TrackPointRepositoryImpl_Factory(trackPointDaoProvider);
  }

  public static TrackPointRepositoryImpl newInstance(TrackPointDao trackPointDao) {
    return new TrackPointRepositoryImpl(trackPointDao);
  }
}
