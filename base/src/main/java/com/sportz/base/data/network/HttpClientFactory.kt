package com.sportz.base.data.network

import com.sportz.base.helper.BaseConfigContract
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.android.AndroidEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * HttpClient factory with better configuration and error handling
 */
@Singleton
class HttpClientFactory @Inject constructor(
    private val baseConfigContract: BaseConfigContract
) {
    
    // Single HttpClient instance for all API calls
    private val _httpClient: HttpClient by lazy {
        HttpClient(Android) {
            installDefaultPlugins()
        }
    }

    /**
     * Gets the HttpClient instance for API calls
     */
    fun getHttpClient(): HttpClient = _httpClient
    
    /**
     * Installs default plugins for HttpClient with improved configuration
     */
    private fun HttpClientConfig<AndroidEngineConfig>.installDefaultPlugins() {
        // Default request configuration
        install(DefaultRequest) {
            headers.append("auth", baseConfigContract.getApiAuthKey())
            headers.append("Content-Type", ContentType.Application.Json.toString())
            url {
                host = baseConfigContract.getBaseUrl()
            }
        }
        
        // Content negotiation with improved JSON configuration
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
                coerceInputValues = true
                allowStructuredMapKeys = true
                explicitNulls = false
            })
        }

        // Logging configuration (only in debug)
        if (baseConfigContract.getIsDebugMode()) {
            install(CurlPrinterPlugin)
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
        }

        // Timeout configuration
        install(HttpTimeout) {
            requestTimeoutMillis = 30000L
            connectTimeoutMillis = 10000L
            socketTimeoutMillis = 10000L
        }
        
        install(HttpRequestRetry) {
            maxRetries = 3
            retryOnException(maxRetries = 2, retryOnTimeout = true)
        }
        
        // Response validation
        install(HttpCallValidator) {
            handleResponseExceptionWithRequest { exception, _ ->
                exception.printStackTrace()
            }
        }
    }
}
