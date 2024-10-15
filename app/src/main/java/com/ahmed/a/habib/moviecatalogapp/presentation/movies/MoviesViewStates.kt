package com.ahmed.a.habib.moviecatalogapp.presentation.movies

import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto

sealed class MoviesViewStates {
    data class MoviesList(val movies: List<MovieDto>) : MoviesViewStates()
}