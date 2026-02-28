package com.traq.feature.onboarding.viewmodel;

import com.traq.core.data.repository.UserPreferencesRepository;
import com.traq.core.permissions.PermissionManager;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<PermissionManager> permissionManagerProvider;

  private final Provider<UserPreferencesRepository> prefsRepositoryProvider;

  public OnboardingViewModel_Factory(Provider<PermissionManager> permissionManagerProvider,
      Provider<UserPreferencesRepository> prefsRepositoryProvider) {
    this.permissionManagerProvider = permissionManagerProvider;
    this.prefsRepositoryProvider = prefsRepositoryProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(permissionManagerProvider.get(), prefsRepositoryProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<PermissionManager> permissionManagerProvider,
      Provider<UserPreferencesRepository> prefsRepositoryProvider) {
    return new OnboardingViewModel_Factory(permissionManagerProvider, prefsRepositoryProvider);
  }

  public static OnboardingViewModel newInstance(PermissionManager permissionManager,
      UserPreferencesRepository prefsRepository) {
    return new OnboardingViewModel(permissionManager, prefsRepository);
  }
}
