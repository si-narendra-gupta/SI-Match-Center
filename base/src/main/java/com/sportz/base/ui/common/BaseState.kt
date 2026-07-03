package com.sportz.base.ui.common

/**
 * Sealed class representing different UI states for a screen
 */
sealed class BaseState<out T> {
    data object Loading : BaseState<Nothing>()
    
    data class Success<T>(val data: T) : BaseState<T>()
    
    data class Error(val message: String, val throwable: Throwable? = null) : BaseState<Nothing>()
    
    data class Empty(val message: String? = null) : BaseState<Nothing>()
}

/**
 * Helper function to handle different states
 */
inline fun <T> BaseState<T>.handle(
    onLoading: () -> Unit = {},
    onEmpty: (String?) -> Unit = {},
    onSuccess: (T) -> Unit = {},
    onError: (String, Throwable?) -> Unit = { _, _ -> }
) {
    when (this) {
        is BaseState.Loading -> onLoading()
        is BaseState.Empty -> onEmpty(message)
        is BaseState.Success -> onSuccess(data)
        is BaseState.Error -> onError(message, throwable)
    }
}
