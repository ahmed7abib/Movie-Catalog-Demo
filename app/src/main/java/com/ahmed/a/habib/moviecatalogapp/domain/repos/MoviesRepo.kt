package com.ahmed.a.habib.moviecatalogapp.domain.repos

import androidx.paging.PagingData
import com.ahmed.a.habib.moviecatalogapp.data.remote.models.Movie
import com.ahmed.a.habib.moviecatalogapp.domain.dto.CurrentPageDto
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepo {
    fun getOnlineMovies(): Flow<PagingData<MovieDto>>

    suspend fun getOnlineMovies(page: Int): Flow<Resource<List<MovieDto>>>

    suspend fun getCurrentPage(): CurrentPageDto?

    suspend fun appendToOfflineMovies(page: Int, moviesList: List<MovieDto>)

    suspend fun deleteAllMovies()
}
