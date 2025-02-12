package com.ahmed.a.habib.moviecatalogapp.di

import android.content.Context
import androidx.room.Room
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.db.MoviesRoomDB
import org.koin.dsl.module

val localModule = module {
    single { provideMovieDao(get()) }
    single { provideMovieDatabase(get()) }
}

private fun provideMovieDatabase(context: Context): MoviesRoomDB {
    return Room.databaseBuilder(
        context.applicationContext,
        MoviesRoomDB::class.java,
        MoviesRoomDB.DATABASE_NAME
    ).build()
}

private fun provideMovieDao(database: MoviesRoomDB): MoviesDao {
    return database.movieDao()
}