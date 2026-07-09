package com.sportz.base.helper

import android.util.Log
import com.sportz.base.data.network.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.content.PartData
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.http.takeFrom
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Helper class for making Ktor API calls with automatic header injection
 */
class KtorServiceHelper(
    private val httpClient: HttpClientFactory
) {
    fun getHttpClient(): HttpClient = httpClient.getHttpClient()

    suspend inline fun <reified T : Any> postApiCall(
        endpoint: String,
        jsonRequestBody: Any? = null,
        formData: List<PartData>? = null,
        method: HttpMethod = HttpMethod.Post,
        additionalHeaders: Map<String, String> = emptyMap(),
        queryParameters: Map<String, String> = emptyMap(),
        contentType: ContentType = ContentType.Application.Json
    ): NetworkResult<T> {
        return this.getHttpClient().safeKtorApiCall {
            when (method) {
                HttpMethod.Post -> {
                    post {
                        url {
                            path(endpoint)
                            queryParameters.forEach { (key, value) ->
                                parameter(key, value)
                            }
                        }

                        // Set content type and body based on request type
                        when {
                            formData != null -> {
                                setBody(MultiPartFormDataContent(formData))
                            }

                            jsonRequestBody != null -> {
                                contentType(contentType)
                                setBody(jsonRequestBody)
                            }
                        }

                        additionalHeaders.forEach { (key, value) ->
                            header(key, value)
                        }
                        Log.d("Headers Network", headers.entries().toString())
                    }
                }

                else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
            }
        }
    }

    suspend inline fun <reified T : Any> getApiCall(
        endpoint: String,
        method: HttpMethod = HttpMethod.Get,
        additionalHeaders: Map<String, String> = emptyMap(),
        queryParameters: Map<String, String> = emptyMap(),
    ): NetworkResult<T> {
        return this.getHttpClient().safeKtorApiCall {
            when (method) {
                HttpMethod.Get -> {
                    get {
                        url {
                            if (endpoint.contains("http")) takeFrom(endpoint)
                            else path(endpoint)
                            queryParameters.forEach { (key, value) ->
                                parameter(key, value)
                            }
                        }

                        additionalHeaders.forEach { (key, value) ->
                            header(key, value)
                        }
                    }
                }

                else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
            }
        }
    }


    suspend inline fun <reified T : Any> HttpClient.safeKtorApiCall(
        crossinline call: suspend HttpClient.() -> HttpResponse
    ): NetworkResult<T> {
        return try {
            val response: HttpResponse = this.call()
            when {
                response.status.isSuccess() -> {
                    try {
                        val body: T = response.body()
                        NetworkResult.Success(body)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        NetworkResult.ApiError(
                            response.status.value, "Failed to parse response: ${e.message}", null
                        )
                    }
                }

                else -> {
                    val errorBody = try {
                        response.body<String>()
                    } catch (e: Exception) {
                        null
                    }
                    NetworkResult.Error(
                        code = response.status.value,
                        message = response.status.description,
                        errorBody
                    )

                }
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {

                is ClientRequestException -> {
                    val errorBody = try {
                        throwable.response.bodyAsText()
                    } catch (ex: Exception) {
                        null
                    }
                    NetworkResult.Error(
                        throwable.response.status.value,
                        throwable.message,
                        errorBody
                    )
                }

                is ServerResponseException -> {
                    val errorBody = try {
                        throwable.response.bodyAsText()
                    } catch (ex: Exception) {
                        null
                    }
                    NetworkResult.Error(
                        throwable.response.status.value,
                        "Server error: ${throwable.message}",
                        errorBody
                    )
                }

                is UnknownHostException -> {
                    NetworkResult.Exception(Throwable(ERROR_MESSAGE_UNKNOWN_HOST_EXCEPTION))
                }

                is ConnectException -> {
                    NetworkResult.Exception(Throwable(ERROR_MESSAGE_CONNECT_EXCEPTION))
                }

                is SocketTimeoutException -> {
                    NetworkResult.Exception(
                        SocketTimeoutException(
                            ERROR_MESSAGE_SOCKET_TIMEOUT_EXCEPTION
                        )
                    )
                }

                is IOException -> {
                    NetworkResult.Exception(Throwable(ERROR_MESSAGE_IO_EXCEPTION))
                }

                // Ktor timeout exceptions
                is java.util.concurrent.TimeoutException -> {
                    NetworkResult.Exception(Throwable(ERROR_MESSAGE_SOCKET_TIMEOUT_EXCEPTION))
                }

                is SerializationException -> {
                    NetworkResult.Exception(
                        SerializationException(
                            "Response parsing failed: ${throwable.message}",
                            throwable
                        )
                    )
                }

                else -> {
                    NetworkResult.Exception(
                        throwable
                    )
                }
            }
        }

    }

}

