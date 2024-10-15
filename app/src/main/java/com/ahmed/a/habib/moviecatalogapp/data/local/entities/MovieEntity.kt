package com.ahmed.a.habib.moviecatalogapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto

@Entity
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int?,
    val title: String?,
    val overview: String?,
    val posterPath: String?,
    val voteCount: Int?,
    val voteAverage: Double?,
    val date: String?,
    val currentPageId: Int? = null
) {
    fun toMovieDto(): MovieDto {
        return MovieDto(
            movieId = movieId,
            title = title,
            overview = overview,
            posterPath = posterPath,
            voteCount = voteCount,
            voteAverage = voteAverage,
            date = date,
            currentPageId = currentPageId
        )
    }
}