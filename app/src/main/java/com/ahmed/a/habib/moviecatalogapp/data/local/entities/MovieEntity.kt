package com.ahmed.a.habib.moviecatalogapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto

@Entity(tableName = "movies")
data class MovieEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "overview")
    val overview: String?,
    @ColumnInfo(name = "posterPath")
    val posterPath: String?,
    @ColumnInfo(name = "voteCount")
    val voteCount: Int?,
    @ColumnInfo(name = "voteAverage")
    val voteAverage: Double?,
    @ColumnInfo(name = "date")
    val date: String?
) {
    fun toMovieDto(): MovieDto {
        return MovieDto(
            id = id,
            title = title,
            overview = overview,
            posterPath = posterPath,
            voteCount = voteCount,
            voteAverage = voteAverage,
            date = date
        )
    }
}