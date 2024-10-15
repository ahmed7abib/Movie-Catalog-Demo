package com.ahmed.a.habib.moviecatalogapp.presentation.movies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.a.habib.moviecatalogapp.R
import com.ahmed.a.habib.moviecatalogapp.databinding.FragmentMoviesBinding
import com.ahmed.a.habib.moviecatalogapp.domain.dto.MovieDto
import com.ahmed.a.habib.moviecatalogapp.utils.Keys
import com.ahmed.a.habib.moviecatalogapp.utils.PaginationListenerKtx
import com.ahmed.a.habib.moviecatalogapp.utils.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : BaseFragment<FragmentMoviesBinding>(FragmentMoviesBinding::inflate) {

    override val viewModel: MoviesViewModel by viewModels()
    private val adapter: MoviesRVAdapter by lazy { MoviesRVAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        render()
        setupRV()
        setupSwipeRefresh()
    }

    private fun init() {
        viewModel.sendIntend(MoviesIntents.GetMovies)
    }

    private fun setupRV() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRV.layoutManager = layoutManager
        binding.moviesRV.adapter = adapter

        binding.moviesRV.addOnScrollListener(object : PaginationListenerKtx(layoutManager, 10) {
            override fun loadMoreItems() {
                viewModel.sendIntend(MoviesIntents.GetOnlineMovies)
            }

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return viewModel.isPaginationLoad
            }
        })

        adapter.listener = { _, item, _ ->
            val bundle = Bundle()
            bundle.putSerializable(Keys.MOVIE, item)
            navigateToWithBundle(R.id.action_moviesFragment_to_movieDetailsFragment, bundle)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.sendIntend(MoviesIntents.RefreshMovies)
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

    private fun handleMoviesList(movies: List<MovieDto>) {
        if (movies.isEmpty()) {
            showEmptyView()
        } else {
            hideEmptyView()
            adapter.submitList(movies)
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