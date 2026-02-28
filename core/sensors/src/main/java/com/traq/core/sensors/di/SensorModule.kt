package com.traq.core.sensors.di

import com.traq.core.sensors.collector.SensorCollector
import com.traq.core.sensors.collector.SensorCollectorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {
    @Provides
    @Singleton
    fun provideSensorCollector(impl: SensorCollectorImpl): SensorCollector = impl
}
