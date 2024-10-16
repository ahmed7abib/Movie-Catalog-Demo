package com.ahmed.a.habib.moviecatalogapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ahmed.a.habib.moviecatalogapp.MainCoroutineRule
import com.ahmed.a.habib.moviecatalogapp.domain.dto.CurrentPageDto
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.getOrAwaitValueTest
import com.ahmed.a.habib.moviecatalogapp.presentation.movies.MoviesViewModel
import com.ahmed.a.habib.moviecatalogapp.presentation.movies.MoviesViewStates
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class MoviesViewModelTestMockito {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: MoviesRepo
    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        repository = Mockito.mock(MoviesRepo::class.java)
        viewModel = MoviesViewModel(repository)
    }

    @Test
    fun `test getMovies calls getCurrentPage when it is not null`() = runTest {
        // Arrange
        val movieList = emptyList<MovieDto>()
        val mockPage = CurrentPageDto(1, movieList)
        Mockito.`when`(repository.getCurrentPage()).thenReturn(mockPage)

        // Act
        viewModel.getMovies()

        // Assert
        val value = viewModel.result.getOrAwaitValueTest()
        assertThat(value).isEqualTo(MoviesViewStates.MoviesList(movieList))
    }

    @Test
    fun `test getMovies calls getOnlineMovies when getCurrentPage is null`() = runTest {
        // Arrange
        val movieList = emptyList<MovieDto>()

        // Tell repo to return null for getCurrentPage and return success for getOnlineMovies
        Mockito.`when`(repository.getCurrentPage()).thenReturn(null)
        Mockito.`when`(repository.getOnlineMovies(1))
            .thenReturn(flowOf(Resource.Success(movieList)))

        // Act
        viewModel.getMovies()

        // Ensure that all coroutines is executed.
        advanceUntilIdle()

        // Assert
        val value = viewModel.result.getOrAwaitValueTest()
        assertThat(value).isEqualTo(MoviesViewStates.MoviesList(movieList))
    }

    @Test
    fun `test getCurrentPage return current offline page success`() = runTest {
        // Arrange
        val mockPage = CurrentPageDto(1, emptyList())

        // Tell repo to return the mock page
        Mockito.`when`(repository.getCurrentPage()).thenReturn(mockPage)

        // Act
        val currentPage = viewModel.getCurrentPage()

        // Assert
        assertThat(currentPage).isEqualTo(mockPage)
    }

    @Test
    fun `test getOnlineMovies fetch movies from online source`() = runTest {
        // Arrange
        val mockPage = CurrentPageDto(1, emptyList())

        // Tell repo to return the mock page
        Mockito.`when`(repository.getOnlineMovies(1))
            .thenReturn(flowOf(Resource.Success(mockPage.moviesList)))

        // Act
        viewModel.getOnlineMovies()

        // Ensure that all coroutines is executed.
        advanceUntilIdle()

        // Assert
        val value = viewModel.result.getOrAwaitValueTest()
        assertThat(value).isEqualTo(MoviesViewStates.MoviesList(mockPage.moviesList))
    }
}