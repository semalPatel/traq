package com.traq.feature.onboarding.viewmodel;

import com.traq.core.data.repository.UserPreferencesRepository;
import com.traq.core.permissions.BatteryOptimizationHelper;
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

  private final Provider<BatteryOptimizationHelper> batteryHelperProvider;

  public OnboardingViewModel_Factory(Provider<PermissionManager> permissionManagerProvider,
      Provider<UserPreferencesRepository> prefsRepositoryProvider,
      Provider<BatteryOptimizationHelper> batteryHelperProvider) {
    this.permissionManagerProvider = permissionManagerProvider;
    this.prefsRepositoryProvider = prefsRepositoryProvider;
    this.batteryHelperProvider = batteryHelperProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(permissionManagerProvider.get(), prefsRepositoryProvider.get(), batteryHelperProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<PermissionManager> permissionManagerProvider,
      Provider<UserPreferencesRepository> prefsRepositoryProvider,
      Provider<BatteryOptimizationHelper> batteryHelperProvider) {
    return new OnboardingViewModel_Factory(permissionManagerProvider, prefsRepositoryProvider, batteryHelperProvider);
  }

  public static OnboardingViewModel newInstance(PermissionManager permissionManager,
      UserPreferencesRepository prefsRepository, BatteryOptimizationHelper batteryHelper) {
    return new OnboardingViewModel(permissionManager, prefsRepository, batteryHelper);
  }
}
