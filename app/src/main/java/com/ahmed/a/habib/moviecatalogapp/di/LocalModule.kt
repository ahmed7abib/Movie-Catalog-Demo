package com.ahmed.a.habib.moviecatalogapp.di

import android.content.Context
import androidx.room.Room
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.db.MoviesRoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MoviesRoomDB {
        return Room.databaseBuilder(
            context.applicationContext,
            MoviesRoomDB::class.java,
            MoviesRoomDB.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MoviesRoomDB): MoviesDao {
        return database.movieDao()
    }
}