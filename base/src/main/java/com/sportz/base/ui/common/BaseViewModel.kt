package com.sportz.base.ui.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface MviIntent {
    data object Load : MviIntent
    data object Refresh : MviIntent
    data object Retry : MviIntent
    data object ClearError : MviIntent
}

interface UiState

abstract class BaseViewModel<T : UiState, I : MviIntent> : ViewModel() {
    
    private val _state = MutableStateFlow<BaseState<T>>(BaseState.Empty())
    val state: StateFlow<BaseState<T>> = _state.asStateFlow()

    /**
     * Updates the UI state
     */
    protected fun updateUiState(newState: BaseState<T>) {
        _state.value = newState
    }

    abstract fun handleIntent(intent: I)

    protected fun setError(message: String, throwable: Throwable? = null) {
        updateUiState(BaseState.Error(message, throwable))
    }

    protected fun setEmpty(message: String? = null) {
        updateUiState(BaseState.Empty(message))
    }

    protected fun setLoading() {
        updateUiState(BaseState.Loading)
    }

    protected fun setSuccess(data: T) {
        updateUiState(BaseState.Success(data))
    }

    /**
     * Executes a suspend function and handles the result with automatic state management
     */
    protected fun <R> executeWithStateManagement(
        block: suspend () -> R,
        onSuccess: (R) -> T,
        onError: (Throwable) -> Unit = { setError(it.message ?: "Unknown error", it) }
    ) {
        viewModelScope.launch {
            setLoading()
            try {
                val result = block()
                if (result == null || (result is Collection<*> && result.isEmpty())) {
                    setEmpty()
                } else {
                    setSuccess(onSuccess(result))
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}
