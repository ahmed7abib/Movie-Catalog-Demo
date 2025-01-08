package com.ahmed.a.habib.moviecatalogapp.presentation.movies

import androidx.paging.PagingData
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto

sealed class MoviesViewStates {
    data class MoviesList(val movies: PagingData<MovieDto>) : MoviesViewStates()
}