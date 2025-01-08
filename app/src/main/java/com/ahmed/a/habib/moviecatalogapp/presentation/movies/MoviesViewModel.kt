package com.ahmed.a.habib.moviecatalogapp.presentation.movies


import androidx.lifecycle.viewModelScope
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.SingleMutableLiveData
import com.ahmed.a.habib.moviecatalogapp.utils.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepo: MoviesRepo) : BaseViewModel() {

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
                    MoviesIntents.RefreshMovies -> refreshMovies()
                }
            }
        }
    }

    private suspend fun getOnlineMovies() {
        moviesRepo.getOnlineMovies(
            errors = { error -> handleError(error) },
            loading = { isLoading -> handleLoading(isLoading) }
        ).collect {
            _result.postValue(MoviesViewStates.MoviesList(it))
        }
    }

    private suspend fun getOfflineMovies() {
        moviesRepo.getOfflineMovies { error ->
            handleError(error)
        }.collect {
            _result.postValue(MoviesViewStates.MoviesList(it))
        }
    }

    suspend fun getMovies() {
        if (offlineMoviesIsNotEmpty()) {
            getOfflineMovies()
        } else {
            getOnlineMovies()
        }
    }

    private fun offlineMoviesIsNotEmpty(): Boolean {
        return true
    }

    private fun refreshMovies() = viewModelScope.launch {
        moviesRepo.deleteAllMovies()
        getOnlineMovies()
    }
}