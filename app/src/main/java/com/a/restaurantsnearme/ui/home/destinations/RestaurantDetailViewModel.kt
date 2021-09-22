package com.a.restaurantsnearme.ui.home.destinations

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.a.restaurantsnearme.model.Restaurant
import com.a.restaurantsnearme.repository.PlacesRepository
import com.a.restaurantsnearme.repository.RepositoryResult
import com.a.restaurantsnearme.service.suspended
import com.a.restaurantsnearme.ui.BaseViewModel
import com.a.restaurantsnearme.utils.Dispatchers
import com.a.restaurantsnearme.utils.SingleLiveEvent
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    val dispatchers: Dispatchers,
    private val placesRepository: PlacesRepository,
    private val fusedLocationClient: FusedLocationProviderClient
) :
    BaseViewModel() {

    private val mutableSearchQuery = MutableStateFlow("")
    private val mutableErrorFetchingNearbySearchLiveData = SingleLiveEvent<Boolean>()
    val errorFetchingNearbySearchLiveData: LiveData<Boolean> = mutableErrorFetchingNearbySearchLiveData
    private val mutableNearbySearchResults = MutableSharedFlow<RepositoryResult<List<Restaurant>, Throwable?>>()

    val placesLiveData = mutableSearchQuery.debounce(500).combine(mutableNearbySearchResults) { query, nearbySearchResult ->
        if (query.isEmpty()) {
            nearbySearchResult
        } else {
            placesRepository.getRestaurantsBySearchQuery(query)
        }
    }.asLiveData()

    init {
        viewModelScope.launch {
            val lastKnownLocation = fusedLocationClient.lastLocation.suspended()
            if (lastKnownLocation != null) {
                mutableNearbySearchResults.emit(placesRepository.getNearbyRestaurants(lastKnownLocation))
            }
        }
    }

    fun onQueryTextChanged(newText: String) {
        viewModelScope.launch {
            mutableSearchQuery.emit(newText)
        }
    }

}