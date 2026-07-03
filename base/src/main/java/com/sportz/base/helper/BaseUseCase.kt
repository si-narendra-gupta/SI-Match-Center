package com.sportz.base.helper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Minimal Base UseCase providing common functionality for all use cases.
 * UseCases should be stateless and provide Flows or suspend functions.
 */
abstract class BaseUseCase {
    
    /**
     * Helper to wrap a suspend block into a Flow with generic error handling
     */
    protected fun <T> resultFlow(block: suspend () -> T): Flow<Result<T>> = flow {
        emit(Result.Loading)
        emit(Result.Success(block()))
    }.catch { e ->
        emit(Result.Error(e))
    }
}
