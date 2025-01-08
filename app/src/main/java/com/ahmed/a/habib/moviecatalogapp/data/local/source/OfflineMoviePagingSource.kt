package com.ahmed.a.habib.moviecatalogapp.data.local.source


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorMessage
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorTypes
import kotlin.collections.orEmpty


class OfflineMoviePagingSource(
    private val moviesDao: MoviesDao,
    private val error: (ErrorTypes) -> Unit,
) : PagingSource<Int, MovieDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieDto>) = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val page = params.key ?: 1

        return try {
            val response = moviesDao.getPage(page = page)

            val moviesList = response?.moviesList?.map { it.toMovieDto() }.orEmpty()

            LoadResult.Page(
                data = moviesList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (moviesList.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
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
}