package com.ahmed.a.habib.moviecatalogapp.domain.dto

import java.io.Serializable

data class MovieDto(
    val id: Int?,
    val title: String?,
    val overview: String?,
    val posterPath: String?,
    val voteCount: Int?,
    val voteAverage: Double?,
    val date: String?
) : Serializable
