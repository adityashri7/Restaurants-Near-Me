package com.a.restaurantsnearme.repository

sealed class RepositoryResult<out D, out E> {
    data class Success<out D>(val data: D) : RepositoryResult<D, Nothing>()
    data class Error<out E>(val error: E) : RepositoryResult<Nothing, E>()
}