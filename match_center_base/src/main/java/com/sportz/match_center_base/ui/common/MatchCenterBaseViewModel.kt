package com.sportz.match_center_base.ui.common

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MviIntent {
    data object Load : MviIntent
    data object Refresh : MviIntent
    data object Retry : MviIntent
    data object ClearError : MviIntent
}

interface UiState

abstract class MatchCenterBaseViewModel<T : UiState, I : MviIntent> : ViewModel() {
    
    private val _state = MutableStateFlow<MatchCenterBaseState<T>>(MatchCenterBaseState.Empty())
    val state: StateFlow<MatchCenterBaseState<T>> = _state.asStateFlow()


    private val _uiState = MutableStateFlow<T?>(null)
    val uiState: StateFlow<T?> = _uiState

    fun getCurrentUiData(): T? {
        return _uiState.value
    }

    fun updateSuccessData(data: T?) {
        _uiState.value = data
        data?.let {
            updateUiState(MatchCenterBaseState.Success(data))
        }

    }

    /**
     * Updates the UI state
     */
    protected fun updateUiState(newState: MatchCenterBaseState<T>) {
        _state.value = newState
    }

    abstract fun handleIntent(intent: I)

    protected fun setError(message: String, throwable: Throwable? = null) {
        updateUiState(MatchCenterBaseState.Error(message, throwable))
    }

    protected fun setEmpty(message: String? = null) {
        updateUiState(MatchCenterBaseState.Empty(message))
    }

    protected fun setLoading() {
        updateUiState(MatchCenterBaseState.Loading)
    }

    protected fun setSuccess(data: T) {
        _uiState.value = data
        updateUiState(MatchCenterBaseState.Success(data))
    }
    /**
     * Executes a suspend function and handles the result
     * Automatically sets loading state before execution and handles errors
     */
    protected suspend fun <R> executeWithLoading(
        onLoading: () -> Unit = { setLoading() },
        onSuccess: (R) -> Unit,
        onError: (Throwable) -> Unit = { setError(it.message ?: "Unknown error", it) },
        block: suspend () -> R
    ) {
        try {
            Log.d("Loading set", "loading is initiallized.")
            onLoading()
            val result = block()
            onSuccess(result)
            Log.d("Loading set", "loading is finished.")
        } catch (e: Exception) {
            onError(e)
        }
    }

    /**
     * Executes a suspend function and handles the result with automatic state management
     * Sets loading before execution, success with data, or error state
     */
    protected suspend fun <R> executeWithStateManagement(
        onSuccess: (R) -> T,
        onError: (Throwable) -> Unit = { setError(it.message ?: "Unknown error", it) },
        block: suspend () -> R
    ) {
        executeWithLoading(
            onSuccess = { result ->
                if (result == null) {
                    setEmpty()
                    return@executeWithLoading
                }
                // 🔥 2. COLLECTION EMPTY CHECK
                if (result is Collection<*> && result.isEmpty()) {
                    setEmpty()
                    return@executeWithLoading
                }
                setSuccess(onSuccess(result))
            },
            onError = onError,
            block = block
        )
    }
}
