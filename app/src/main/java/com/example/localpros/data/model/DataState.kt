package com.example.localpros.data.model

sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
}
