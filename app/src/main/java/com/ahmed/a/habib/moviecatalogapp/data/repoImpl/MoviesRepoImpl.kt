package com.ahmed.a.habib.moviecatalogapp.data.repoImpl

import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints.API_KEY
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.network.BaseRemoteDataSource
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import kotlinx.coroutines.flow.Flow
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
                    deleteAllMovies()
                    saveMovies(moviesList)
                    Resource.Success(moviesList)
                } else {
                    Resource.Success(emptyList())
                }
            }

            is Resource.Error -> {
                Resource.Error(it.errorTypes)
            }
        }
    }

    override suspend fun saveMovies(moviesList: List<MovieDto>) {

        val moviesEntityList = moviesList.map {
            MovieEntity(
                id = it.id,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath,
                voteCount = it.voteCount,
                voteAverage = it.voteAverage,
                date = it.date
            )
        }

        moviesEntityList.forEach {
            moviesDao.saveMovies(it)
        }
    }

    override fun getOfflineMovies(): Flow<List<MovieDto>> {
        return moviesDao.getAllMovies().map { moviesList -> moviesList.map { it.toMovieDto() } }
    }

    override suspend fun deleteAllMovies() {
        moviesDao.deleteAllMovies()
    }
}