package com.a.restaurantsnearme.utils

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

class DefaultDispatchers : Dispatchers {
    override val main = kotlinx.coroutines.Dispatchers.Main
    override val io = kotlinx.coroutines.Dispatchers.IO
    override val default = kotlinx.coroutines.Dispatchers.Default
    override val unconfined = kotlinx.coroutines.Dispatchers.Unconfined
}