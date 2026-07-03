package com.sportz.base.helper


sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>()
    data class ApiError<out T: Any>(val code: Int?, val message: String, val data: T?) : NetworkResult<T>()
    data class Error(val code: Int?, val message: String, val body: String? = null) : NetworkResult<Nothing>()
    data class Exception(val e: Throwable) : NetworkResult<Nothing>()
}

/**
 * Centralized mapping from ApiResult<T> to domain Result<R>
 */
inline fun <T, R> ApiResult<T>.mapToDomainResult(
    transform: (T) -> R
): Result<R> {
    return when (this) {
        is ApiResult.Success -> try {
            Result.Success(transform(data))
        } catch (e: java.lang.Exception) {
            Result.Error(e)
        }

        is ApiResult.Error -> {
            Result.Error(java.lang.Exception("API Error: $message (Code: $code)"))
        }
        
        is ApiResult.Exception -> {
            Result.Error(throwable)
        }

        is ApiResult.Loading -> Result.Loading
    }
}
