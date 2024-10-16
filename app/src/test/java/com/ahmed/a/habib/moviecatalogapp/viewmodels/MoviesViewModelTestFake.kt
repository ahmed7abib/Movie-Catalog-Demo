package com.ahmed.a.habib.moviecatalogapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ahmed.a.habib.moviecatalogapp.MainCoroutineRule
import com.ahmed.a.habib.moviecatalogapp.domain.dto.CurrentPageDto
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.getOrAwaitValueTest
import com.ahmed.a.habib.moviecatalogapp.presentation.movies.MoviesViewModel
import com.ahmed.a.habib.moviecatalogapp.presentation.movies.MoviesViewStates
import com.ahmed.a.habib.moviecatalogapp.repo.MoviesFakeRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MoviesViewModelTestFake {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MoviesViewModel

    @Before
    fun setUp() {
        viewModel = MoviesViewModel(MoviesFakeRepo())
    }

    @Test
    fun `test getMovies success`() = runTest {
        // Arrange
        val movieList = emptyList<MovieDto>()

        // Act
        viewModel.getMovies()

        // Assert
        val value = viewModel.result.getOrAwaitValueTest()
        assertThat(value).isEqualTo(MoviesViewStates.MoviesList(movieList))
    }

    @Test
    fun `test getCurrentPage return current offline page success`() = runTest {
        // Arrange
        val mockPage = CurrentPageDto(1, emptyList())

        // Act
        val currentPage = viewModel.getCurrentPage()

        // Assert
        assertThat(currentPage).isEqualTo(mockPage)
    }

    @Test
    fun `test getOnlineMovies fetch movies from online source`() = runTest {
        // Arrange
        val mockPage = CurrentPageDto(1, emptyList())

        // Act
        viewModel.getOnlineMovies()

        // Ensure that all coroutines is executed.
        advanceUntilIdle()

        // Assert
        val value = viewModel.result.getOrAwaitValueTest()
        assertThat(value).isEqualTo(MoviesViewStates.MoviesList(mockPage.moviesList))
    }
}