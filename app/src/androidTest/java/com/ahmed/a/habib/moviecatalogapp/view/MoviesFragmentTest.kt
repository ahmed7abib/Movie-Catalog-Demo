package com.ahmed.a.habib.moviecatalogapp.view

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.ahmed.a.habib.moviecatalogapp.MainCoroutineRule
import com.ahmed.a.habib.moviecatalogapp.MoviesFragmentFactory
import com.ahmed.a.habib.moviecatalogapp.R
import com.ahmed.a.habib.moviecatalogapp.databinding.ItemListMoviesBinding
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.launchFragmentInHiltContainer
import com.ahmed.a.habib.moviecatalogapp.presentation.movies.MoviesFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MoviesFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Inject
    lateinit var fragmentFactory: MoviesFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testRecyclerViewItemClick() = runTest {
        // Arrange
        val navController = Mockito.mock(NavController::class.java)
        val argumentCaptor = ArgumentCaptor.forClass(Bundle::class.java)

        val fakeMovies = listOf(
            MovieDto(
                1,
                1,
                "title",
                "posterPath",
                "overview",
                122,
                4.5,
                "releaseDate"
            )
        )

        launchFragmentInHiltContainer<MoviesFragment>(factory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            adapter.submitList(fakeMovies)
        }

        // Act
        onView(withId(R.id.moviesRV))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<BaseRVAdapter.BaseViewHolder<ItemListMoviesBinding>>(
                    0,
                    click()
                )
            )

        // Assert
        Mockito.verify(navController).navigate(
            Mockito.eq(R.id.action_moviesFragment_to_movieDetailsFragment),
            argumentCaptor.capture()
        )
    }
}