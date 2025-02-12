package com.ahmed.a.habib.moviecatalogapp.di

import com.ahmed.a.habib.moviecatalogapp.data.MoviesRepoImpl
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import org.koin.dsl.module


val reposModule = module {
    single { MoviesRepoImpl(get(), get()) as MoviesRepo }
}