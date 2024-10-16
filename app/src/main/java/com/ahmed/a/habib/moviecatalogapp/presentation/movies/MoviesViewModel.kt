package com.ahmed.a.habib.moviecatalogapp.presentation.movies

import androidx.lifecycle.viewModelScope
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.SingleMutableLiveData
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import com.ahmed.a.habib.moviecatalogapp.utils.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepo: MoviesRepo) : BaseViewModel() {

    private var _pageNumber = 1

    private var _isPaginationLoad = false
    val isPaginationLoad: Boolean get() = _isPaginationLoad

    private var _isLastPage = false
    val isLastPage: Boolean get() = _isLastPage

    private val sendIntend: MutableSharedFlow<MoviesIntents> = MutableSharedFlow()

    private val _result: SingleMutableLiveData<MoviesViewStates> = SingleMutableLiveData()
    val result: SingleMutableLiveData<MoviesViewStates> get() = _result

    fun sendIntend(intent: MoviesIntents) = viewModelScope.launch {
        sendIntend.emit(intent)
    }

    init {
        viewModelScope.launch {
            sendIntend.collectLatest {
                when (it) {
                    MoviesIntents.GetMovies -> getMovies()
                    MoviesIntents.GetOnlineMovies -> getOnlineMovies()
                    MoviesIntents.RefreshMovies -> refreshMovies()
                }
            }
        }
    }

    private fun refreshMovies() = viewModelScope.launch {
        _pageNumber = 1
        _isLastPage = false
        moviesRepo.deleteAllMovies()
        getOnlineMovies()
    }

    suspend fun getMovies() {
        val currentPage = getCurrentPage()

        if (currentPage != null) {
            _pageNumber = currentPage.page
            val movieList = currentPage.moviesList
            _result.postValue(MoviesViewStates.MoviesList(movieList))
        } else {
            getOnlineMovies()
        }
    }

    suspend fun getCurrentPage() = moviesRepo.getCurrentPage()

    suspend fun getOnlineMovies() {
        moviesRepo.getOnlineMovies(_pageNumber++).onStart {
            _isPaginationLoad = true
            handleLoading(true)
        }.map {
            when (it) {
                is Resource.Error -> {
                    _isPaginationLoad = false
                    handleError(it.errorTypes)
                }

                is Resource.Success -> {
                    _isPaginationLoad = false
                    handleLoading(false)

                    if (it.data.orEmpty().isEmpty()) {
                        _isLastPage = true
                        _pageNumber = 1
                    }

                    _result.postValue(MoviesViewStates.MoviesList(it.data.orEmpty()))
                }
            }
        }.launchIn(viewModelScope)
    }
}