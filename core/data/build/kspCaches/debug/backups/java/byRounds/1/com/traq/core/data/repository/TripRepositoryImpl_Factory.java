package com.traq.core.data.repository;

import com.traq.core.data.db.dao.TripDao;
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
public final class TripRepositoryImpl_Factory implements Factory<TripRepositoryImpl> {
  private final Provider<TripDao> tripDaoProvider;

  public TripRepositoryImpl_Factory(Provider<TripDao> tripDaoProvider) {
    this.tripDaoProvider = tripDaoProvider;
  }

  @Override
  public TripRepositoryImpl get() {
    return newInstance(tripDaoProvider.get());
  }

  public static TripRepositoryImpl_Factory create(Provider<TripDao> tripDaoProvider) {
    return new TripRepositoryImpl_Factory(tripDaoProvider);
  }

  public static TripRepositoryImpl newInstance(TripDao tripDao) {
    return new TripRepositoryImpl(tripDao);
  }
}
