package com.ahmed.a.habib.moviecatalogapp.utils.network

sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val data: T?) : Resource<T>()
    data class Error(val errorTypes: ErrorTypes) : Resource<Nothing>()
}