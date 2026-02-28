package com.traq.core.location.di

import com.traq.core.location.controller.TrackingController
import com.traq.core.location.controller.TrackingControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides
    @Singleton
    fun provideTrackingController(impl: TrackingControllerImpl): TrackingController = impl
}
