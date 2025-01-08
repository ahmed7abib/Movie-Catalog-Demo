package com.ahmed.a.habib.moviecatalogapp.data.remote.source


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.PageEntity
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorMessage
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorTypes
import kotlin.collections.orEmpty


class OnlineMoviePagingSource(
    private val moviesDao: MoviesDao,
    private val apiService: MoviesApi,
    private val error: (ErrorTypes) -> Unit,
    private val loading: (Boolean) -> Unit,
) : PagingSource<Int, MovieDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieDto>) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val page = params.key ?: 1

        loading(true)

        return try {
            loading(false)
            val response = apiService.getMovies(page = page)

            if (response.isSuccessful) {
                val moviesList = response.body()?.moviesList?.map { it.toMovieDto() }.orEmpty()

                appendToOfflineMovies(page, moviesList)

                LoadResult.Page(
                    data = moviesList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (moviesList.isEmpty()) null else page + 1
                )
            } else {
                error(
                    ErrorTypes.GeneralError(
                        ErrorMessage.DynamicString(
                            response.message()
                        )
                    )
                )

                LoadResult.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            loading(false)

            error(
                ErrorTypes.GeneralError(
                    ErrorMessage.DynamicString(
                        e.message.orEmpty()
                    )
                )
            )

            LoadResult.Error(Exception(e.message))
        }
    }

    private suspend fun appendToOfflineMovies(page: Int, list: List<MovieDto>) {
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
        val allOfflineMovies = moviesDao.getPage(page)?.moviesList.orEmpty()
        moviesDao.deleteAllPages()

        newOfflineMoviesList.addAll(allOfflineMovies)
        newOfflineMoviesList.addAll(currentMovieList)

        moviesDao.saveCurrentPage(PageEntity(pageNumber = page, moviesList = newOfflineMoviesList))
    }
}