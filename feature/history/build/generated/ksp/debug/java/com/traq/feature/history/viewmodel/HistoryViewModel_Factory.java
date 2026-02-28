package com.traq.feature.history.viewmodel;

import com.traq.core.data.repository.TripRepository;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<TripRepository> tripRepositoryProvider;

  public HistoryViewModel_Factory(Provider<TripRepository> tripRepositoryProvider) {
    this.tripRepositoryProvider = tripRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(tripRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(Provider<TripRepository> tripRepositoryProvider) {
    return new HistoryViewModel_Factory(tripRepositoryProvider);
  }

  public static HistoryViewModel newInstance(TripRepository tripRepository) {
    return new HistoryViewModel(tripRepository);
  }
}
