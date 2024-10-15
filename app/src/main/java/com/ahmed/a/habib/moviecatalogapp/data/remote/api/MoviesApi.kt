package com.ahmed.a.habib.moviecatalogapp.data.remote.api

import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints.MOVIES
import com.ahmed.a.habib.moviecatalogapp.data.remote.models.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesApi {

    @GET(MOVIES)
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<MoviesResponse>
}