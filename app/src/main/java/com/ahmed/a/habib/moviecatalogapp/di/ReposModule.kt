package com.ahmed.a.habib.moviecatalogapp.di

import com.ahmed.a.habib.moviecatalogapp.data.local.dao.MoviesDao
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import com.ahmed.a.habib.moviecatalogapp.data.repoImpl.MoviesRepoImpl
import com.ahmed.a.habib.moviecatalogapp.domain.repos.MoviesRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ReposModule {

    @Provides
    fun provideMoviesRepo(moviesApi: MoviesApi, moviesDao: MoviesDao): MoviesRepo {
        return MoviesRepoImpl(moviesApi, moviesDao)
    }
}