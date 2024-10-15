package com.ahmed.a.habib.moviecatalogapp.domain.repos

import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepo {
    suspend fun getOnlineMovies(page: Int): Flow<Resource<List<MovieDto>>>

    fun getOfflineMovies(): Flow<List<MovieDto>>

    suspend fun saveMovies(moviesList: List<MovieDto>)

    suspend fun deleteAllMovies()
}
