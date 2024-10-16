package com.ahmed.a.habib.moviecatalogapp.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ahmed.a.habib.moviecatalogapp.data.local.MoviesRoomDB
import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.CurrentPageEntity
import com.ahmed.a.habib.moviecatalogapp.data.local.entities.MovieEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MoviesDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var roomDatabase: MoviesRoomDB

    private lateinit var dao: MoviesDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = roomDatabase.movieDao()
    }

    @After
    fun teardown() {
        roomDatabase.close()
    }

    @Test
    fun testSaveCurrentPage() = runTest {
        // Arrange
        val currentPageEntity =
            CurrentPageEntity(1, 1, listOf(MovieEntity(1, "", "", "", 122, 1.5, "", 1)))

        // Act
        dao.saveCurrentPage(currentPageEntity)
        val currentPage = dao.getCurrentPage()

        // Assert
        assertThat(currentPage).isEqualTo(currentPageEntity)
    }

    @Test
    fun testDeleteAllPages() = runTest {
        // Arrange
        val currentPageEntity =
            CurrentPageEntity(1, 1, listOf(MovieEntity(1, "", "", "", 122, 1.5, "", 1)))

        // Act
        dao.saveCurrentPage(currentPageEntity)
        dao.deleteAllPages()
        val currentPage = dao.getCurrentPage()

        // Assert
        assertThat(currentPage).isNotEqualTo(currentPageEntity)
    }
}