package com.a.restaurantsnearme.service

import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal inline fun <R> runAuthenticatedRemoteCatching(block: () -> R): ServiceResult<R, Throwable?> {
    return try {
        ServiceResult.Success(block())
    } catch (e: Throwable) {
        ServiceResult.Error(e)
    }
}

suspend fun <Response> Task<Response>.suspended(): Response = suspendCoroutine { continuation ->
    addOnSuccessListener { continuation.resume(it) }
    addOnFailureListener { continuation.resumeWithException(it) }
}