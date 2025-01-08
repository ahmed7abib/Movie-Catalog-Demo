package com.ahmed.a.habib.moviecatalogapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ahmed.a.habib.moviecatalogapp.domain.dto.CurrentPageDto

@Entity(tableName = "pages")
data class PageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "pageNumber")
    val pageNumber: Int,
    @ColumnInfo(name = "moviesList")
    val moviesList: List<MovieEntity>
) {
    fun toDto(): CurrentPageDto {
        return CurrentPageDto(
            page = pageNumber,
            moviesList = moviesList.map { it.toMovieDto() }
        )
    }
}