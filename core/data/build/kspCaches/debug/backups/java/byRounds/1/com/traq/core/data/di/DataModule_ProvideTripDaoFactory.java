package com.traq.core.data.di;

import com.traq.core.data.db.TraqDatabase;
import com.traq.core.data.db.dao.TripDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DataModule_ProvideTripDaoFactory implements Factory<TripDao> {
  private final Provider<TraqDatabase> dbProvider;

  public DataModule_ProvideTripDaoFactory(Provider<TraqDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TripDao get() {
    return provideTripDao(dbProvider.get());
  }

  public static DataModule_ProvideTripDaoFactory create(Provider<TraqDatabase> dbProvider) {
    return new DataModule_ProvideTripDaoFactory(dbProvider);
  }

  public static TripDao provideTripDao(TraqDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideTripDao(db));
  }
}
