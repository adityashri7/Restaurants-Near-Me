package com.a.restaurantsnearme.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.a.restaurantsnearme.repository.PlacesRepository
import com.a.restaurantsnearme.ui.home.destinations.*
import com.a.restaurantsnearme.utils.Dispatchers
import com.google.android.gms.location.FusedLocationProviderClient

class HomeViewModelFactory(
    private val dispatchers: Dispatchers,
    private val placesRepository: PlacesRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CheckForLocationPermissionsViewModel::class.java -> CheckForLocationPermissionsViewModel() as T
            RequestLocationPermissionsViewModel::class.java -> RequestLocationPermissionsViewModel() as T
            RestaurantListViewModel::class.java -> RestaurantListViewModel(dispatchers, placesRepository, fusedLocationProviderClient) as T
            RestaurantMapViewModel::class.java -> RestaurantMapViewModel(dispatchers, placesRepository, fusedLocationProviderClient) as T
            RestaurantDetailViewModel::class.java -> RestaurantDetailViewModel() as T
            else -> throw IllegalArgumentException("Unsupported Model Class: $modelClass")
        }
    }
}