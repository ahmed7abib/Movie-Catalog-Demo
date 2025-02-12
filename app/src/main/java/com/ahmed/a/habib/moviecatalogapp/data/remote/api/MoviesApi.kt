package com.ahmed.a.habib.moviecatalogapp.data.remote.api

import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints.API_KEY
import com.ahmed.a.habib.moviecatalogapp.data.remote.api.EndPoints.MOVIES
import com.ahmed.a.habib.moviecatalogapp.data.remote.models.MoviesResponse
import com.ahmed.a.habib.moviecatalogapp.utils.network.BaseRemoteDataSource
import com.ahmed.a.habib.moviecatalogapp.utils.network.Resource
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow


class MoviesApi(private val httpClient: HttpClient) : BaseRemoteDataSource(httpClient) {

    fun getMovies(page: Int): Flow<Resource<MoviesResponse>> = safeApiCall(
        deserializedType = object : TypeToken<MoviesResponse>() {}.type
    ) {
        httpClient.get(MOVIES) {
            parameter("api_key", API_KEY)
            parameter("page", page)
        }
    }
}