package com.a.restaurantsnearme.ui.home.destinations

import com.a.restaurantsnearme.ui.BaseViewModel
import com.a.restaurantsnearme.ui.home.MapListData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckForLocationPermissionsViewModel @Inject constructor() : BaseViewModel() {

    fun userHasLocationPermissions() {
        navigate(CheckForLocationPermissionsFragmentDirections.goToRestaurantListViewFragment(MapListData()))
    }

    fun userDoesNotHaveLocationPermissions() {
        navigate(CheckForLocationPermissionsFragmentDirections.goToRequestLocationPermissionsFragment())
    }
}