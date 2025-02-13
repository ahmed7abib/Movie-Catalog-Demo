package com.ahmed.a.habib.moviecatalogapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.flatMap
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import com.ahmed.a.habib.moviecatalogapp.data.remote.source.OnlineMoviePagingSource
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class MoviesRepoImpl(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao,
) : MoviesRepo {

    override suspend fun getOnlineMovies(
        errors: (ErrorTypes) -> Unit,
        loading: (Boolean) -> Unit,
    ): Flow<PagingData<MovieDto>> {
        return Pager(
            pagingSourceFactory = {
                OnlineMoviePagingSource(
                    moviesDao,
                    moviesApi,
                    error = { error -> errors(error) },
                    loading = { loading -> loading(loading) }
                )
            },
            config = PagingConfig(pageSize = 10, enablePlaceholders = false)
        ).flow
    }


    override suspend fun getOfflineMovies(): Flow<PagingData<MovieDto>> {
        return Pager(
            pagingSourceFactory = { moviesDao.getPage() },
            config = PagingConfig(pageSize = 10, enablePlaceholders = false)
        ).flow.map { pagingData ->
            pagingData.flatMap { pageEntity -> pageEntity.toDto().moviesList }
        }
    }

    override suspend fun deleteAllMovies() {
        moviesDao.deleteAllPages()
    }

    override suspend fun hasStoredPages() = moviesDao.getPageCount() > 0
}