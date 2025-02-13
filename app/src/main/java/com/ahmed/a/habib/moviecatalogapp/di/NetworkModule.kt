package com.ahmed.a.habib.moviecatalogapp.di

import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.MoviesApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson
import org.koin.dsl.module


val networkModule = module {
    single { provideHttpClient() }
    single { provideApiService(get()) }
}

private fun provideHttpClient(): HttpClient {
    return HttpClient(CIO) {
        defaultRequest { url(EndPoints.BASE_URL) }
        install(ContentNegotiation) { gson() }
        install(Logging) {
            level = LogLevel.BODY
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR $message")
                }
            }
        }
    }
}

private fun provideApiService(client: HttpClient): MoviesApi {
    return MoviesApi(client)
}