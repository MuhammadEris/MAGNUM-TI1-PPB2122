package com.magnum_ti1.plant.common

sealed class Response<out T> {
    class Loading<out T> : Response<T>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Fail<out T>(val e: Exception): Response<T>()
}
