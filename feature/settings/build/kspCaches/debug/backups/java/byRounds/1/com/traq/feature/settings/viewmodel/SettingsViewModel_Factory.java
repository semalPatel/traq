package com.traq.feature.settings.viewmodel;

import com.traq.core.data.repository.UserPreferencesRepository;
import com.traq.core.data.util.StorageCalculator;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UserPreferencesRepository> prefsProvider;

  private final Provider<PermissionManager> permissionManagerProvider;

  private final Provider<BatteryOptimizationHelper> batteryHelperProvider;

  private final Provider<StorageCalculator> storageCalculatorProvider;

  public SettingsViewModel_Factory(Provider<UserPreferencesRepository> prefsProvider,
      Provider<PermissionManager> permissionManagerProvider,
      Provider<BatteryOptimizationHelper> batteryHelperProvider,
      Provider<StorageCalculator> storageCalculatorProvider) {
    this.prefsProvider = prefsProvider;
    this.permissionManagerProvider = permissionManagerProvider;
    this.batteryHelperProvider = batteryHelperProvider;
    this.storageCalculatorProvider = storageCalculatorProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(prefsProvider.get(), permissionManagerProvider.get(), batteryHelperProvider.get(), storageCalculatorProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<UserPreferencesRepository> prefsProvider,
      Provider<PermissionManager> permissionManagerProvider,
      Provider<BatteryOptimizationHelper> batteryHelperProvider,
      Provider<StorageCalculator> storageCalculatorProvider) {
    return new SettingsViewModel_Factory(prefsProvider, permissionManagerProvider, batteryHelperProvider, storageCalculatorProvider);
  }

  public static SettingsViewModel newInstance(UserPreferencesRepository prefs,
      PermissionManager permissionManager, BatteryOptimizationHelper batteryHelper,
      StorageCalculator storageCalculator) {
    return new SettingsViewModel(prefs, permissionManager, batteryHelper, storageCalculator);
  }
}
