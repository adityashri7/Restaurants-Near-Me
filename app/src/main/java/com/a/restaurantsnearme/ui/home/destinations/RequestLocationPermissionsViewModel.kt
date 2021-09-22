package com.a.restaurantsnearme.ui.home.destinations

import com.a.restaurantsnearme.ui.BaseViewModel
import com.a.restaurantsnearme.ui.home.MapListData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RequestLocationPermissionsViewModel @Inject constructor() : BaseViewModel() {

    fun userHasLocationAccess() {
        navigate(RequestLocationPermissionsFragmentDirections.goToRestaurantListViewFragment(MapListData()))
    }
}