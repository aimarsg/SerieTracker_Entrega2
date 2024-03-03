package com.aimarsg.serietracker.di

import android.content.Context
import androidx.room.Room
import com.aimarsg.serietracker.data.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, Database::class.java, "serieTracker")
            .fallbackToDestructiveMigration()
            .build()
}