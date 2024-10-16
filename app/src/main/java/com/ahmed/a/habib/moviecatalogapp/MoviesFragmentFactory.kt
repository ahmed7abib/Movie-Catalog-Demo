package com.ahmed.a.habib.moviecatalogapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.ahmed.a.habib.moviecatalogapp.presentation.movieDetails.MovieDetailsFragment
import com.ahmed.a.habib.moviecatalogapp.presentation.movies.MoviesFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MoviesFragmentFactory @Inject constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            MoviesFragment::class.java.name -> MoviesFragment()
            MovieDetailsFragment::class.java.name -> MovieDetailsFragment()
            else -> super.instantiate(classLoader, className)
        }
    }
}