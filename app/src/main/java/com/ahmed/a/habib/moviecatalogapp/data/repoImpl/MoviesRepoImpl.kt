package com.ahmed.a.habib.moviecatalogapp.data.repoImpl

import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.CurrentPageEntity
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints.API_KEY
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import com.ahmed.a.habib.moviecatalogapp.domain.dto.CurrentPageDto
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.network.BaseRemoteDataSource
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import kotlinx.coroutines.flow.map

class MoviesRepoImpl(private val moviesApi: MoviesApi, private val moviesDao: MoviesDao) :
    MoviesRepo, BaseRemoteDataSource() {

    override suspend fun getOnlineMovies(page: Int) = safeApiCall {
        moviesApi.getMovies(API_KEY, page)
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
        moviesDao.deleteAllMovies()
    }
}