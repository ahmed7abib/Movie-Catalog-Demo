package com.ahmed.a.habib.moviecatalogapp.utils.network

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.text.isEmpty
import kotlin.text.orEmpty

open class BaseRemoteDataSource(
    private val httpClient: HttpClient,
    private val gson: Gson = Gson(),
) {

    fun <T : Any> safeApiCall(
        deserializedType: Type?,
        request: suspend HttpClient.() -> HttpResponse,
    ): Flow<Resource<T>> = flow {
        emit(safeApiResult(request, deserializedType))
    }

    private suspend fun <T : Any> safeApiResult(
        request: suspend HttpClient.() -> HttpResponse,
        deserializedType: Type?,
    ): Resource<T> {
        var response: HttpResponse? = null

        try {
            response = httpClient.request()
        } catch (_: Exception) {
            return getResultError(null)
        }

        if (response.status.isSuccess()) {
            val responseBody = response.bodyAsText()

            return try {
                val result: T = gson.fromJson(responseBody, deserializedType)
                Resource.Success(result)
            } catch (_: Exception) {
                getResultError(null)
            }
        }

        return getResultError(response)
    }

    private suspend fun <T : Any> getResultError(response: HttpResponse?): Resource<T> {
        return when (response?.status) {
            HttpStatusCode.Unauthorized -> {
                Resource.Error(ErrorTypes.AuthenticationError())
            }

            in listOf(
                HttpStatusCode.PaymentRequired,
                HttpStatusCode.Forbidden,
                HttpStatusCode.NotFound
            ),
                -> {
                val message: String = try {
                    val responseBody = response?.bodyAsText().orEmpty()
                    val jObjError = JSONObject(responseBody)
                    val statusObj = jObjError.optJSONObject("Status")
                    statusObj?.optString("Message").orEmpty()
                } catch (_: Exception) {
                    "Error happened, try again."
                }

                Resource.Error(
                    ErrorTypes.NetworkError(
                        ErrorMessage.DynamicString(message)
                    )
                )
            }

            HttpStatusCode.InternalServerError -> {
                Resource.Error(
                    ErrorTypes.NetworkError(
                        ErrorMessage.DynamicString("Error happened, please try again later")
                    )
                )
            }

            else -> {
                val message = response?.bodyAsText().orEmpty()

                if (message.isEmpty()) {
                    Resource.Error(
                        ErrorTypes.GeneralError(
                            ErrorMessage.DynamicString("Error happened. Please check your internet and try again.")
                        )
                    )
                } else {
                    Resource.Error(ErrorTypes.GeneralError(ErrorMessage.DynamicString(message)))
                }
            }
        }
    }
}
