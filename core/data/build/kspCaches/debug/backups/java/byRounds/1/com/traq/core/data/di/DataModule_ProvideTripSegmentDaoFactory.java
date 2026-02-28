package com.traq.core.data.di;

import com.traq.core.data.db.TraqDatabase;
import com.traq.core.data.db.dao.TripSegmentDao;
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
public final class DataModule_ProvideTripSegmentDaoFactory implements Factory<TripSegmentDao> {
  private final Provider<TraqDatabase> dbProvider;

  public DataModule_ProvideTripSegmentDaoFactory(Provider<TraqDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TripSegmentDao get() {
    return provideTripSegmentDao(dbProvider.get());
  }

  public static DataModule_ProvideTripSegmentDaoFactory create(Provider<TraqDatabase> dbProvider) {
    return new DataModule_ProvideTripSegmentDaoFactory(dbProvider);
  }

  public static TripSegmentDao provideTripSegmentDao(TraqDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideTripSegmentDao(db));
  }
}
