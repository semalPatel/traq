package com.traq.core.ai.di

import com.traq.core.ai.classification.RuleBasedClassifier
import com.traq.core.ai.classification.TransportClassifier
import com.traq.core.ai.deadreckoning.DeadReckoningEngine
import com.traq.core.ai.deadreckoning.DeadReckoningEngineImpl
import com.traq.core.ai.filter.GpsProcessor
import com.traq.core.ai.filter.GpsProcessorImpl
import com.traq.core.ai.sampling.AdaptiveSampler
import com.traq.core.ai.sampling.RuleBasedSampler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideGpsProcessor(impl: GpsProcessorImpl): GpsProcessor = impl

    @Provides
    @Singleton
    fun provideAdaptiveSampler(impl: RuleBasedSampler): AdaptiveSampler = impl

    @Provides
    @Singleton
    fun provideTransportClassifier(impl: RuleBasedClassifier): TransportClassifier = impl

    @Provides
    @Singleton
    fun provideDeadReckoningEngine(impl: DeadReckoningEngineImpl): DeadReckoningEngine = impl
}
