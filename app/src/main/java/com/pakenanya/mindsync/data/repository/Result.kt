package com.pakenanya.mindsync.data.repository

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}