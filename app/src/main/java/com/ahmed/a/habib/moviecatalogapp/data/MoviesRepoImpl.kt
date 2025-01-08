package com.ahmed.a.habib.moviecatalogapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.source.OfflineMoviePagingSource
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import com.ahmed.a.habib.moviecatalogapp.data.remote.source.OnlineMoviePagingSource
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.network.BaseRemoteDataSource
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorTypes


class MoviesRepoImpl(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao,
) : MoviesRepo, BaseRemoteDataSource() {

    override suspend fun getOnlineMovies(
        errors: (ErrorTypes) -> Unit,
        loading: (Boolean) -> Unit
    ) = Pager(
        pagingSourceFactory = {
            OnlineMoviePagingSource(
                moviesDao, moviesApi,
                error = { error -> errors(error) },
                loading = { loading -> loading(loading) }
            )
        },
        config = PagingConfig(pageSize = 10, enablePlaceholders = false)
    ).flow

    override suspend fun getOfflineMovies(
        errors: (ErrorTypes) -> Unit
    ) = Pager(
        pagingSourceFactory = {
            OfflineMoviePagingSource(
                moviesDao,
                error = { error -> errors(error) },
            )
        },
        config = PagingConfig(pageSize = 10, enablePlaceholders = false)
    ).flow

    override suspend fun deleteAllMovies() {
        moviesDao.deleteAllPages()
    }
}