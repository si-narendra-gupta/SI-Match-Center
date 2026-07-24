package com.sportz.match_center_base.ui.common

/**
 * Sealed class representing different UI states for a screen
 */
sealed class MatchCenterBaseState<out T> {
    data object Loading : MatchCenterBaseState<Nothing>()
    
    data class Success<T>(val data: T) : MatchCenterBaseState<T>()
    
    data class Error(val message: String, val throwable: Throwable? = null) : MatchCenterBaseState<Nothing>()
    
    data class Empty(val message: String? = null) : MatchCenterBaseState<Nothing>()
}

/**
 * Helper function to handle different states
 */
inline fun <T> MatchCenterBaseState<T>.handle(
    onLoading: () -> Unit = {},
    onEmpty: (String?) -> Unit = {},
    onSuccess: (T) -> Unit = {},
    onError: (String, Throwable?) -> Unit = { _, _ -> }
) {
    when (this) {
        is MatchCenterBaseState.Loading -> onLoading()
        is MatchCenterBaseState.Empty -> onEmpty(message)
        is MatchCenterBaseState.Success -> onSuccess(data)
        is MatchCenterBaseState.Error -> onError(message, throwable)
    }
}
