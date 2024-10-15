package com.ahmed.a.habib.moviecatalogapp.domain.dto

data class CurrentPageDto(
    val page: Int,
    val moviesList: List<MovieDto>
)
