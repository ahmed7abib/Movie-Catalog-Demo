package com.ahmed.a.habib.moviecatalogapp.di

import com.ahmed.a.habib.moviecatalogapp.presentation.movies.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MoviesViewModel(get()) }
}