package com.ahmed.a.habib.moviecatalogapp.repo

import com.ahmed.a.habib.moviecatalogapp.domain.dto.CurrentPageDto
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MoviesFakeRepo : MoviesRepo {

    override suspend fun getOnlineMovies(page: Int): Flow<Resource<List<MovieDto>>> {
        return flowOf(Resource.Success(emptyList()))
    }

    override suspend fun getCurrentPage(): CurrentPageDto {
        return CurrentPageDto(1, emptyList())
    }

    override suspend fun appendToOfflineMovies(page: Int, moviesList: List<MovieDto>) {
        // Not need to test it here
        // I will test dao itself.
    }

    override suspend fun deleteAllMovies() {
        // Not need to test it here
        // I will test dao itself.
    }
}