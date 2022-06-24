package com.varun.grogu.data.utils

import com.varun.grogu.domain.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

/**
 * Generic util function to get [Resource] of suspend block result.
 *
 * @param apiCall: Suspend block which needs to runs safely
 * @param coroutineDispatcher: Context with the block will be run with
 */
suspend inline fun <T> safeApiCallWithCoroutine(coroutineDispatcher: CoroutineDispatcher, crossinline apiCall: suspend () -> T) = withContext(coroutineDispatcher) {
    try {
        Resource.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException -> Resource.Failure(throwable, throwable.code())
            is IOException -> Resource.Failure(throwable, 0)
            else -> Resource.Failure(throwable, -1)
        }
    }
}

/**
 * Generic util function to get [Flow] of [Resource] of suspend block result.
 *
 * @param apiCall: Suspend block which needs to runs safely
 * @param coroutineDispatcher: Context with the block will be run with
 */
suspend inline fun <T> safeApiCallWithFlow(coroutineDispatcher: CoroutineDispatcher, crossinline apiCall: suspend () -> T) = flow {
    try {
        emit(Resource.Loading())
        val result = withContext(coroutineDispatcher) {
            apiCall.invoke()
        }
        emit(Resource.Success(result))
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException -> emit(Resource.Failure(throwable, throwable.code()))
            is IOException -> emit(Resource.Failure(throwable, 0))
            else -> emit(Resource.Failure(throwable, -1))
        }
    }
}