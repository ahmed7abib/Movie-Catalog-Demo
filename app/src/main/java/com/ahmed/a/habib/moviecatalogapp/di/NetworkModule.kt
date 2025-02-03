package com.ahmed.a.habib.moviecatalogapp.di

import android.util.Log
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) { gson() }
            defaultRequest { url(EndPoints.BASE_URL) }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i("KTOR", message)
                    }
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(client: HttpClient): MoviesApi {
        return MoviesApi(client)
    }
}