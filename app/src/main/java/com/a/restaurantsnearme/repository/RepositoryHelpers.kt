package com.a.restaurantsnearme.repository

import com.a.restaurantsnearme.service.ServiceResult

inline fun <RESPONSE, RESULT> ServiceResult<RESPONSE, Throwable?>.toRepositoryResult(block: (serviceResponse: RESPONSE) -> RESULT): RepositoryResult<RESULT, Throwable?> {
    return when (this) {
        is ServiceResult.Success -> RepositoryResult.Success(block(data))
        is ServiceResult.Error -> RepositoryResult.Error(error)
    }
}