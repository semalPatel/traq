package com.traq.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.traq.core.common.model.ExportFormat;
import com.traq.core.data.db.TraqDatabase;
import com.traq.core.data.db.dao.TrackPointDao;
import com.traq.core.data.db.dao.TripDao;
import com.traq.core.data.di.DataModule_ProvideDataStoreFactory;
import com.traq.core.data.di.DataModule_ProvideDatabaseFactory;
import com.traq.core.data.di.DataModule_ProvideTrackPointDaoFactory;
import com.traq.core.data.di.DataModule_ProvideTrackPointRepositoryFactory;
import com.traq.core.data.di.DataModule_ProvideTripDaoFactory;
import com.traq.core.data.di.DataModule_ProvideTripRepositoryFactory;
import com.traq.core.data.di.DataModule_ProvideUserPreferencesRepositoryFactory;
import com.traq.core.data.repository.TrackPointRepository;
import com.traq.core.data.repository.TrackPointRepositoryImpl;
import com.traq.core.data.repository.TripRepository;
import com.traq.core.data.repository.TripRepositoryImpl;
import com.traq.core.data.repository.UserPreferencesRepository;
import com.traq.core.data.repository.UserPreferencesRepositoryImpl;
import com.traq.core.export.api.ExportManager;
import com.traq.core.export.api.TripExporter;
import com.traq.core.export.di.ExportModule_ProvideExportManagerFactory;
import com.traq.core.export.di.ExportModule_ProvideExportersFactory;
import com.traq.core.export.format.GeoJsonExporter;
import com.traq.core.export.format.GpxExporter;
import com.traq.core.export.format.KmlExporter;
import com.traq.core.export.format.TraqExporter;
import com.traq.core.export.manager.ExportManagerImpl;
import com.traq.core.location.controller.TrackingController;
import com.traq.core.location.controller.TrackingControllerImpl;
import com.traq.core.location.di.LocationModule_ProvideTrackingControllerFactory;
import com.traq.core.location.provider.LocationProvider;
import com.traq.core.location.service.TrackingNotificationManager;
import com.traq.core.location.service.TrackingService;
import com.traq.core.location.service.TrackingService_MembersInjector;
import com.traq.core.location.util.BatteryMonitor;
import com.traq.core.location.util.WakeLockManager;
import com.traq.core.maps.api.MapRenderer;
import com.traq.core.maps.di.MapModule_ProvideMapRendererFactory;
import com.traq.core.maps.google.GoogleMapsRenderer;
import com.traq.core.maps.maplibre.MapLibreRenderer;
import com.traq.core.permissions.PermissionManager;
import com.traq.feature.dashboard.viewmodel.DashboardViewModel;
import com.traq.feature.dashboard.viewmodel.DashboardViewModel_HiltModules;
import com.traq.feature.dashboard.viewmodel.DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.traq.feature.dashboard.viewmodel.DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.traq.feature.history.viewmodel.HistoryViewModel;
import com.traq.feature.history.viewmodel.HistoryViewModel_HiltModules;
import com.traq.feature.history.viewmodel.HistoryViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.traq.feature.history.viewmodel.HistoryViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.traq.feature.onboarding.viewmodel.OnboardingViewModel;
import com.traq.feature.onboarding.viewmodel.OnboardingViewModel_HiltModules;
import com.traq.feature.onboarding.viewmodel.OnboardingViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.traq.feature.onboarding.viewmodel.OnboardingViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.traq.feature.settings.viewmodel.SettingsViewModel;
import com.traq.feature.settings.viewmodel.SettingsViewModel_HiltModules;
import com.traq.feature.settings.viewmodel.SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.traq.feature.settings.viewmodel.SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.traq.feature.tracking.viewmodel.TrackingViewModel;
import com.traq.feature.tracking.viewmodel.TrackingViewModel_HiltModules;
import com.traq.feature.tracking.viewmodel.TrackingViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.traq.feature.tracking.viewmodel.TrackingViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import com.traq.feature.tripdetail.viewmodel.TripDetailViewModel;
import com.traq.feature.tripdetail.viewmodel.TripDetailViewModel_HiltModules;
import com.traq.feature.tripdetail.viewmodel.TripDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey;
import com.traq.feature.tripdetail.viewmodel.TripDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerTraqApplication_HiltComponents_SingletonC {
  private DaggerTraqApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public TraqApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements TraqApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public TraqApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements TraqApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public TraqApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements TraqApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public TraqApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements TraqApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public TraqApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements TraqApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public TraqApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements TraqApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public TraqApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements TraqApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public TraqApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends TraqApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends TraqApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends TraqApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends TraqApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
      injectMainActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(6).put(DashboardViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, DashboardViewModel_HiltModules.KeyModule.provide()).put(HistoryViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, HistoryViewModel_HiltModules.KeyModule.provide()).put(OnboardingViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, OnboardingViewModel_HiltModules.KeyModule.provide()).put(SettingsViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, SettingsViewModel_HiltModules.KeyModule.provide()).put(TrackingViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, TrackingViewModel_HiltModules.KeyModule.provide()).put(TripDetailViewModel_HiltModules_KeyModule_Provide_LazyMapKey.lazyClassKeyName, TripDetailViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectMapRenderer(instance, singletonCImpl.provideMapRendererProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends TraqApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<TrackingViewModel> trackingViewModelProvider;

    private Provider<TripDetailViewModel> tripDetailViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.trackingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.tripDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(6).put(DashboardViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) dashboardViewModelProvider)).put(HistoryViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) historyViewModelProvider)).put(OnboardingViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) onboardingViewModelProvider)).put(SettingsViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) settingsViewModelProvider)).put(TrackingViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) trackingViewModelProvider)).put(TripDetailViewModel_HiltModules_BindsModule_Binds_LazyMapKey.lazyClassKeyName, ((Provider) tripDetailViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.traq.feature.dashboard.viewmodel.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.provideTripRepositoryProvider.get(), singletonCImpl.provideTrackingControllerProvider.get());

          case 1: // com.traq.feature.history.viewmodel.HistoryViewModel 
          return (T) new HistoryViewModel(singletonCImpl.provideTripRepositoryProvider.get());

          case 2: // com.traq.feature.onboarding.viewmodel.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.permissionManagerProvider.get(), singletonCImpl.provideUserPreferencesRepositoryProvider.get());

          case 3: // com.traq.feature.settings.viewmodel.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.provideUserPreferencesRepositoryProvider.get());

          case 4: // com.traq.feature.tracking.viewmodel.TrackingViewModel 
          return (T) new TrackingViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.provideTrackingControllerProvider.get(), singletonCImpl.provideTrackPointRepositoryProvider.get());

          case 5: // com.traq.feature.tripdetail.viewmodel.TripDetailViewModel 
          return (T) new TripDetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.provideTripRepositoryProvider.get(), singletonCImpl.provideTrackPointRepositoryProvider.get(), singletonCImpl.provideExportManagerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends TraqApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends TraqApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    private LocationProvider locationProvider() {
      return new LocationProvider(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));
    }

    private TrackingNotificationManager trackingNotificationManager() {
      return new TrackingNotificationManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));
    }

    private WakeLockManager wakeLockManager() {
      return new WakeLockManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));
    }

    @Override
    public void injectTrackingService(TrackingService arg0) {
      injectTrackingService2(arg0);
    }

    private TrackingService injectTrackingService2(TrackingService instance) {
      TrackingService_MembersInjector.injectLocationProvider(instance, locationProvider());
      TrackingService_MembersInjector.injectTrackPointRepository(instance, singletonCImpl.provideTrackPointRepositoryProvider.get());
      TrackingService_MembersInjector.injectTripRepository(instance, singletonCImpl.provideTripRepositoryProvider.get());
      TrackingService_MembersInjector.injectNotificationManager(instance, trackingNotificationManager());
      TrackingService_MembersInjector.injectWakeLockManager(instance, wakeLockManager());
      TrackingService_MembersInjector.injectBatteryMonitor(instance, singletonCImpl.batteryMonitor());
      return instance;
    }
  }

  private static final class SingletonCImpl extends TraqApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<DataStore<Preferences>> provideDataStoreProvider;

    private Provider<UserPreferencesRepository> provideUserPreferencesRepositoryProvider;

    private Provider<MapRenderer> provideMapRendererProvider;

    private Provider<TraqDatabase> provideDatabaseProvider;

    private Provider<TripRepository> provideTripRepositoryProvider;

    private Provider<TrackingControllerImpl> trackingControllerImplProvider;

    private Provider<TrackingController> provideTrackingControllerProvider;

    private Provider<PermissionManager> permissionManagerProvider;

    private Provider<TrackPointRepository> provideTrackPointRepositoryProvider;

    private Provider<ExportManager> provideExportManagerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private UserPreferencesRepositoryImpl userPreferencesRepositoryImpl() {
      return new UserPreferencesRepositoryImpl(provideDataStoreProvider.get());
    }

    private MapLibreRenderer mapLibreRenderer() {
      return new MapLibreRenderer(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule));
    }

    private TripDao tripDao() {
      return DataModule_ProvideTripDaoFactory.provideTripDao(provideDatabaseProvider.get());
    }

    private TripRepositoryImpl tripRepositoryImpl() {
      return new TripRepositoryImpl(tripDao());
    }

    private BatteryMonitor batteryMonitor() {
      return new BatteryMonitor(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule));
    }

    private TrackPointDao trackPointDao() {
      return DataModule_ProvideTrackPointDaoFactory.provideTrackPointDao(provideDatabaseProvider.get());
    }

    private TrackPointRepositoryImpl trackPointRepositoryImpl() {
      return new TrackPointRepositoryImpl(trackPointDao());
    }

    private Map<ExportFormat, TripExporter> mapOfExportFormatAndTripExporter() {
      return ExportModule_ProvideExportersFactory.provideExporters(new TraqExporter(), new GpxExporter(), new GeoJsonExporter(), new KmlExporter());
    }

    private ExportManagerImpl exportManagerImpl() {
      return new ExportManagerImpl(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule), provideTripRepositoryProvider.get(), provideTrackPointRepositoryProvider.get(), mapOfExportFormatAndTripExporter());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 2));
      this.provideUserPreferencesRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UserPreferencesRepository>(singletonCImpl, 1));
      this.provideMapRendererProvider = DoubleCheck.provider(new SwitchingProvider<MapRenderer>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<TraqDatabase>(singletonCImpl, 4));
      this.provideTripRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TripRepository>(singletonCImpl, 3));
      this.trackingControllerImplProvider = DoubleCheck.provider(new SwitchingProvider<TrackingControllerImpl>(singletonCImpl, 6));
      this.provideTrackingControllerProvider = DoubleCheck.provider(new SwitchingProvider<TrackingController>(singletonCImpl, 5));
      this.permissionManagerProvider = DoubleCheck.provider(new SwitchingProvider<PermissionManager>(singletonCImpl, 7));
      this.provideTrackPointRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TrackPointRepository>(singletonCImpl, 8));
      this.provideExportManagerProvider = DoubleCheck.provider(new SwitchingProvider<ExportManager>(singletonCImpl, 9));
    }

    @Override
    public void injectTraqApplication(TraqApplication arg0) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.traq.core.maps.api.MapRenderer 
          return (T) MapModule_ProvideMapRendererFactory.provideMapRenderer(singletonCImpl.provideUserPreferencesRepositoryProvider.get(), new GoogleMapsRenderer(), singletonCImpl.mapLibreRenderer());

          case 1: // com.traq.core.data.repository.UserPreferencesRepository 
          return (T) DataModule_ProvideUserPreferencesRepositoryFactory.provideUserPreferencesRepository(singletonCImpl.userPreferencesRepositoryImpl());

          case 2: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) DataModule_ProvideDataStoreFactory.provideDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.traq.core.data.repository.TripRepository 
          return (T) DataModule_ProvideTripRepositoryFactory.provideTripRepository(singletonCImpl.tripRepositoryImpl());

          case 4: // com.traq.core.data.db.TraqDatabase 
          return (T) DataModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.traq.core.location.controller.TrackingController 
          return (T) LocationModule_ProvideTrackingControllerFactory.provideTrackingController(singletonCImpl.trackingControllerImplProvider.get());

          case 6: // com.traq.core.location.controller.TrackingControllerImpl 
          return (T) new TrackingControllerImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideTripRepositoryProvider.get(), singletonCImpl.batteryMonitor());

          case 7: // com.traq.core.permissions.PermissionManager 
          return (T) new PermissionManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // com.traq.core.data.repository.TrackPointRepository 
          return (T) DataModule_ProvideTrackPointRepositoryFactory.provideTrackPointRepository(singletonCImpl.trackPointRepositoryImpl());

          case 9: // com.traq.core.export.api.ExportManager 
          return (T) ExportModule_ProvideExportManagerFactory.provideExportManager(singletonCImpl.exportManagerImpl());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
