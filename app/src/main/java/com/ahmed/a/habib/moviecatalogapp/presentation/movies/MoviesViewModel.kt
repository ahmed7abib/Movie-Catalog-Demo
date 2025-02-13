package com.ahmed.a.habib.moviecatalogapp.presentation.movies

import androidx.lifecycle.viewModelScope
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import com.ahmed.a.habib.moviecatalogapp.utils.SingleMutableLiveData
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorMessage
import com.ahmed.a.habib.moviecatalogapp.utils.network.ErrorTypes
import com.ahmed.a.habib.moviecatalogapp.utils.ui.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesViewModel(private val moviesRepo: MoviesRepo) : BaseViewModel() {

    private val sendIntend: MutableSharedFlow<MoviesIntents> = MutableSharedFlow()

    private val _result: SingleMutableLiveData<MoviesViewStates> = SingleMutableLiveData()
    val result: SingleMutableLiveData<MoviesViewStates> get() = _result

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(ErrorTypes.GeneralError(ErrorMessage.DynamicString(throwable.message.orEmpty())))
    }

    fun sendIntend(intent: MoviesIntents) = viewModelScope.launch {
        sendIntend.emit(intent)
    }

    init {
        viewModelScope.launch {
            sendIntend.collect {
                when (it) {
                    MoviesIntents.GetMovies -> getMovies()
                    MoviesIntents.RefreshMovies -> {
                        deleteAllMovies()
                        getMovies()
                    }
                }
            }
        }
    }

    fun getMovies() = viewModelScope.launch(coroutineExceptionHandler) {
        val hasStoredPages = offlineMoviesIsNotEmpty()

        if (hasStoredPages) {
            emmitOfflineMovies()
        } else {
            emmitOnlineMovies()
        }
    }

    private suspend fun emmitOnlineMovies() {
        getOnlineMovies(
            errors = { error -> handleError(error) },
            loading = { isLoading -> handleLoading(isLoading) }
        ).collectLatest {
            _result.value = MoviesViewStates.MoviesList(it)
        }
    }

    suspend fun getOnlineMovies(
        errors: (ErrorTypes) -> Unit,
        loading: (Boolean) -> Unit,
    ) = withContext(Dispatchers.IO) {
        moviesRepo.getOnlineMovies(errors = { errors(it) }, loading = { loading(it) })
    }

    private suspend fun emmitOfflineMovies() {
        getOfflineMovies().collect { _result.value = MoviesViewStates.MoviesList(it) }
    }

    private suspend fun getOfflineMovies() = withContext(Dispatchers.IO) {
        moviesRepo.getOfflineMovies()
    }

    private suspend fun offlineMoviesIsNotEmpty() = withContext(Dispatchers.IO) {
        moviesRepo.hasStoredPages()
    }

    private suspend fun deleteAllMovies() = withContext(Dispatchers.IO) {
        moviesRepo.deleteAllMovies()
    }
}