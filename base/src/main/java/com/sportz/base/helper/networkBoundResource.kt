package com.sportz.base.helper

import kotlinx.coroutines.flow.*

/**
 * A generic function that can provide a resource backed by both the sqlite database and the network.
 * Offline-first implementation:
 * 1. Emit data from local source.
 * 2. If shouldFetch is true, perform network fetch.
 * 3. Save fetch result to local source.
 * 4. Emit updated data from local source.
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
): Flow<ApiResult<ResultType>> = flow {
    
    // Step 1: Emit local data
    val data = query().first()
    emit(ApiResult.Success(data))

    // Step 2: Check if we should fetch from network
    if (shouldFetch(data)) {
        emit(ApiResult.Loading)
        try {
            // Step 3: Fetch and save
            val fetchedData = fetch()
            saveFetchResult(fetchedData)
            
            // Step 4: Emit new data from local source
            emitAll(query().map { ApiResult.Success(it) })
        } catch (throwable: Throwable) {
            // Emit error but keep previous data if possible
            emit(ApiResult.Exception(throwable))
            emitAll(query().map { ApiResult.Success(it) })
        }
    } else {
        // Just keep observing local data
        emitAll(query().map { ApiResult.Success(it) })
    }
}
