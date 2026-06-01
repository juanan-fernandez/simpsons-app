package com.example.bigschoolexample.domain.model

sealed interface NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>
    data class Error(
        val exception: Throwable,
        val message: String,
        val kind: NetworkErrorKind = NetworkErrorKind.Unknown,
    ) : NetworkResult<Nothing>
    data object Loading : NetworkResult<Nothing>
}
