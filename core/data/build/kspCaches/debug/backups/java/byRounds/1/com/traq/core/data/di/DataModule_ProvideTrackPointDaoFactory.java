package com.traq.core.data.di;

import com.traq.core.data.db.TraqDatabase;
import com.traq.core.data.db.dao.TrackPointDao;
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
public final class DataModule_ProvideTrackPointDaoFactory implements Factory<TrackPointDao> {
  private final Provider<TraqDatabase> dbProvider;

  public DataModule_ProvideTrackPointDaoFactory(Provider<TraqDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TrackPointDao get() {
    return provideTrackPointDao(dbProvider.get());
  }

  public static DataModule_ProvideTrackPointDaoFactory create(Provider<TraqDatabase> dbProvider) {
    return new DataModule_ProvideTrackPointDaoFactory(dbProvider);
  }

  public static TrackPointDao provideTrackPointDao(TraqDatabase db) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideTrackPointDao(db));
  }
}
