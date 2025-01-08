package com.ahmed.a.habib.moviecatalogapp.domain.repos

import androidx.paging.PagingData
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorTypes
import kotlinx.coroutines.flow.Flow

interface MoviesRepo {

    suspend fun getOnlineMovies(
        errors: (ErrorTypes) -> Unit,
        loading: (Boolean) -> Unit,
    ): Flow<PagingData<MovieDto>>

    suspend fun getOfflineMovies(
        errors: (ErrorTypes) -> Unit,
    ): Flow<PagingData<MovieDto>>

    suspend fun deleteAllMovies()
}
