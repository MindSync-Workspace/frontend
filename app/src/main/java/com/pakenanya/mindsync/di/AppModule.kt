package com.pakenanya.mindsync.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import android.app.Application
import android.content.Context
import dagger.hilt.components.SingletonComponent
import com.pakenanya.mindsync.data.manager.MindSyncAppPreferences

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMentalQAppPreferences(context: Context): MindSyncAppPreferences {
        return MindSyncAppPreferences(context)
    }
}