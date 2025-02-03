package com.ahmed.a.habib.moviecatalogapp.data.remote.api

import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints.API_KEY
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints.MOVIES
import com.ahmed.a.habib.moviecatalogapp.data.remote.models.MoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter


class MoviesApi(private val httpClient: HttpClient) {

    suspend fun getMovies(page: Int): Result<MoviesResponse> {
        return try {
            Result.success(
                httpClient.get(MOVIES) {
                    parameter("api_key", API_KEY)
                    parameter("page", page)
                }.body()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}