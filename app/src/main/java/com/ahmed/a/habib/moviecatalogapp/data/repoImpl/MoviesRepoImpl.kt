package com.ahmed.a.habib.moviecatalogapp.data.repoImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.CurrentPageEntity
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviePagingSource
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import com.ahmed.a.habib.moviecatalogapp.domain.dto.CurrentPageDto
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.network.BaseRemoteDataSource
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepoImpl(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao,
    private val moviesPagingSource: MoviePagingSource,
) : MoviesRepo, BaseRemoteDataSource() {

    override fun getOnlineMovies(): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { moviesPagingSource }
        ).flow
    }

    override suspend fun getOnlineMovies(page: Int) = safeApiCall {
        moviesApi.getMovies(page = page)
    }.map {
        when (it) {
            is Resource.Success -> {

                val responseList = it.data?.moviesList

                if (responseList.orEmpty().isNotEmpty()) {
                    val moviesList = responseList?.map { movie -> movie.toMovieDto() }.orEmpty()
                    appendToOfflineMovies(page, moviesList)
                    val offlineMoviesList = moviesDao.getCurrentPage()?.toDto()?.moviesList
                    Resource.Success(offlineMoviesList)
                } else {
                    Resource.Success(emptyList())
                }
            }

            is Resource.Error -> {
                Resource.Error(it.errorTypes)
            }
        }
    }

    override suspend fun appendToOfflineMovies(page: Int, list: List<MovieDto>) {
        val currentMovieList = list.map {
            MovieEntity(
                movieId = it.movieId,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath,
                voteCount = it.voteCount,
                voteAverage = it.voteAverage,
                date = it.date
            )
        }

        val newOfflineMoviesList = arrayListOf<MovieEntity>()
        val allOfflineMovies = moviesDao.getCurrentPage()?.moviesList.orEmpty()
        deleteAllMovies()

        newOfflineMoviesList.addAll(allOfflineMovies)
        newOfflineMoviesList.addAll(currentMovieList)

        moviesDao.saveCurrentPage(CurrentPageEntity(page = page, moviesList = newOfflineMoviesList))
    }

    override suspend fun getCurrentPage(): CurrentPageDto? {
        return moviesDao.getCurrentPage()?.toDto()
    }

    override suspend fun deleteAllMovies() {
        moviesDao.deleteAllPages()
    }
}