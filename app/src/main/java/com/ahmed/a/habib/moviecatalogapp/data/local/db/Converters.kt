package com.ahmed.a.habib.moviecatalogapp.data.local.db

import androidx.room.TypeConverter
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity
import com.google.gson.Gson

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromMovieListToJson(movies: List<MovieEntity>): String {
        return gson.toJson(movies)
    }

    @TypeConverter
    fun fromJsonToMovieList(json: String): List<MovieEntity> {
        val type = object : com.google.gson.reflect.TypeToken<List<MovieEntity>>() {}.type
        return gson.fromJson(json, type)
    }
}