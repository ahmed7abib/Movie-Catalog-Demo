package com.ahmed.a.habib.moviecatalogapp.presentation.movies

sealed class MoviesIntents {
    data object GetMovies : MoviesIntents()
    data object GetOnlineMovies : MoviesIntents()
}