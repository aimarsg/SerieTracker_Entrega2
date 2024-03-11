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

/**
 * This is the hilt module. this module is installed in singletoncomponent, meaning that all the instance here are stored in
 * the application level, so they arent destroyed until the app is closed and can be shared between activitites
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * GENERATE ROOM DATABASE
     */
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, Database::class.java, "serieTracker")
            .createFromAsset("database/serieTracker.db")
            .fallbackToDestructiveMigration()
            .build()


    /**
    DATABASE REPOSITORIES
     */
    @Singleton
    @Provides
    fun provideSerieCatalogoDao(db:Database) = db.serieCatalogoDao()

    @Singleton
    @Provides
    fun provideSerieUsuarioDao(db:Database) = db.serieUsuarioDao()
}