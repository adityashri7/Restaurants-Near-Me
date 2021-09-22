package com.a.restaurantsnearme.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.a.restaurantsnearme.utils.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {

    private val navigationEvent = SingleLiveEvent<NavDirections>()

    fun getNavigationEvent(): LiveData<NavDirections> = navigationEvent

    fun navigate(navDestination: NavDirections) {
        navigationEvent.postValue(navDestination)
    }
}