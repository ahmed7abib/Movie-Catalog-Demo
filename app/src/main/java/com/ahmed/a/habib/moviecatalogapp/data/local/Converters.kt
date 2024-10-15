package com.ahmed.a.habib.moviecatalogapp.data.local

import androidx.room.TypeConverter
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromMovieListToJson(movies: List<MovieEntity>): String {
        return gson.toJson(movies)
    }

    @TypeConverter
    fun fromJsonToMovieList(json: String): List<MovieEntity> {
        val type = object : TypeToken<List<MovieEntity>>() {}.type
        return gson.fromJson(json, type)
    }
}