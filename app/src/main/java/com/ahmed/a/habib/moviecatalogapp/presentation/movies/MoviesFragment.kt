package com.ahmed.a.habib.moviecatalogapp.presentation.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.a.habib.moviecatalogapp.R
import com.ahmed.a.habib.moviecatalogapp.databinding.FragmentMoviesBinding
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.Keys
import com.ahmed.a.habib.moviecatalogapp.utils.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {

    override val viewModel: MoviesViewModel by viewModels()
    val adapter: MoviesPagingAdapter by lazy { MoviesPagingAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.sendIntend(MoviesIntents.GetMovies)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        render()
        setupRV()
        setupSwipeRefresh()
    }

    private fun setupRV() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRV.layoutManager = layoutManager
        binding.moviesRV.adapter = adapter

        adapter.listener = { _, item, _ ->
            val bundle = Bundle()
            bundle.putSerializable(Keys.MOVIE, item)
            navigateToWithBundle(R.id.action_moviesFragment_to_movieDetailsFragment, bundle)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.sendIntend(MoviesIntents.RefreshMovies)
            adapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun render() {
        viewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is MoviesViewStates.MoviesList -> handleMoviesList(it.movies)
            }
        }
    }

    private fun handleMoviesList(movies: PagingData<MovieDto>?) =
        lifecycleScope.launch(Dispatchers.Main) {
            if (movies == null) {
                showEmptyView()
            } else {
                hideEmptyView()
                adapter.submitData(movies)
            }
        }

    private fun showEmptyView() {
        binding.moviesRV.visibility = View.GONE
        binding.emptyTV.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        binding.emptyTV.visibility = View.GONE
        binding.moviesRV.visibility = View.VISIBLE
    }
}