package com.ahmed.a.habib.moviecatalogapp.presentation.movieDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.ahmed.a.habib.moviecatalogapp.databinding.FragmentMovieDetailsBinding
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.Keys
import com.ahmed.a.habib.moviecatalogapp.utils.loadImage
import com.ahmed.a.habib.moviecatalogapp.utils.serializable
import com.ahmed.a.habib.moviecatalogapp.utils.ui.BaseFragment
import com.ahmed.a.habib.moviecatalogapp.utils.ui.BaseViewModel


class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate) {

    override val viewModel: BaseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        val movie = arguments?.serializable<MovieDto>(Keys.MOVIE) as MovieDto

        binding.apply {
            titleTV.text = movie.title
            overViewTV.text = movie.overview
            ratingTV.text = movie.voteAverage.toString()
            posterIV.loadImage(movie.posterPath.orEmpty())
        }
    }
}