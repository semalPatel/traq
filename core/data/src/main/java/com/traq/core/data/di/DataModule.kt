package com.traq.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.traq.core.data.db.TraqDatabase
import com.traq.core.data.db.dao.TrackPointDao
import com.traq.core.data.db.dao.TripDao
import com.traq.core.data.db.dao.TripSegmentDao
import com.traq.core.data.repository.TrackPointRepository
import com.traq.core.data.repository.TrackPointRepositoryImpl
import com.traq.core.data.repository.TripRepository
import com.traq.core.data.repository.TripRepositoryImpl
import com.traq.core.data.repository.UserPreferencesRepository
import com.traq.core.data.repository.UserPreferencesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TraqDatabase =
        Room.databaseBuilder(context, TraqDatabase::class.java, "traq.db").build()

    @Provides
    fun provideTripDao(db: TraqDatabase): TripDao = db.tripDao()

    @Provides
    fun provideTrackPointDao(db: TraqDatabase): TrackPointDao = db.trackPointDao()

    @Provides
    fun provideTripSegmentDao(db: TraqDatabase): TripSegmentDao = db.tripSegmentDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("traq_prefs") }

    @Provides
    @Singleton
    fun provideTripRepository(impl: TripRepositoryImpl): TripRepository = impl

    @Provides
    @Singleton
    fun provideTrackPointRepository(impl: TrackPointRepositoryImpl): TrackPointRepository = impl

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository = impl
}
