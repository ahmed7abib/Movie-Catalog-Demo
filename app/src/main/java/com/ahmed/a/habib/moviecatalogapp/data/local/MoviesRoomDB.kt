package com.ahmed.a.habib.moviecatalogapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MoviesRoomDB : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "movies_database"
    }

    abstract fun movieDao(): MoviesDao
}