package com.ahmed.a.habib.moviecatalogapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.PageEntity
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity

@Database(entities = [MovieEntity::class, PageEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MoviesRoomDB : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "movies_database"
    }

    abstract fun movieDao(): MoviesDao
}