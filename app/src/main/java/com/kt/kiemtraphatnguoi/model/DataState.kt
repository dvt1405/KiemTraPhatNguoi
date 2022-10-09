package com.kt.kiemtraphatnguoi.model

sealed class DataState<T> {
    data class Success<T>(val data: T) : DataState<T>()
    class Loading<T>: DataState<T>()
    data class Error<T>(val throwable: Throwable) : DataState<T>()
}