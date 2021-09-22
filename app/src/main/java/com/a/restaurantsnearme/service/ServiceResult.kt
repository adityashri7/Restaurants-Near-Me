package com.a.restaurantsnearme.service

sealed class ServiceResult<out D, out E> {
    data class Success<out D>(val data: D) : ServiceResult<D, Nothing>()
    data class Error<out E>(val error: E) : ServiceResult<Nothing, E>()
}