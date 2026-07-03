package com.sportz.base.helper

/**
 * Enhanced result wrapper for data layer with better error handling
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val message: String, val body: String? = null) : ApiResult<Nothing>()
    data class Exception(val throwable: Throwable) : ApiResult<Nothing>()
    data object Loading : ApiResult<Nothing>()
}

/**
 * Convert NetworkResult to ApiResult
 */
inline fun <reified T : Any> NetworkResult<T>.toApiResult(): ApiResult<T> {
    return when (this) {
        is NetworkResult.Success -> ApiResult.Success(data)
        is NetworkResult.ApiError -> ApiResult.Error(code ?: 0, message, data?.toString())
        is NetworkResult.Error -> ApiResult.Error(code ?: 0, message, body)
        is NetworkResult.Exception -> ApiResult.Exception(e)
    }
}

/**
 * Domain layer Result
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}
