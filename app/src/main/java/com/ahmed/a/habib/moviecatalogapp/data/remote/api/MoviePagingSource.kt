package com.ahmed.a.habib.moviecatalogapp.data.remote.api


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto

class MoviePagingSource(
    private val apiService: MoviesApi,
) : PagingSource<Int, MovieDto>() {

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val page = params.key ?: 1

        Log.d("TAG", "load: aaa" + page)

        return try {
            val response = apiService.getMovies(page = page)

            if (response.isSuccessful) {
                val moviesList = response.body()?.moviesList?.map { it.toMovieDto() }.orEmpty()

                LoadResult.Page(
                    data = moviesList,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (moviesList.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}