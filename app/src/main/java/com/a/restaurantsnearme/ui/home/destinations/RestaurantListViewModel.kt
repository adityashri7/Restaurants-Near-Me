package com.a.restaurantsnearme.ui.home.destinations

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.a.restaurantsnearme.model.Restaurant
import com.a.restaurantsnearme.repository.PlacesRepository
import com.a.restaurantsnearme.repository.RepositoryResult
import com.a.restaurantsnearme.repository.RepositoryResult.Success
import com.a.restaurantsnearme.service.suspended
import com.a.restaurantsnearme.ui.BaseViewModel
import com.a.restaurantsnearme.ui.home.MapListData
import com.a.restaurantsnearme.utils.Dispatchers
import com.a.restaurantsnearme.utils.SingleLiveEvent
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val dispatchers: Dispatchers,
    private val placesRepository: PlacesRepository,
    private val fusedLocationClient: FusedLocationProviderClient
) : BaseViewModel() {

    private val mutableSearchQuery = MutableStateFlow("")
    private val mutableErrorFetchingNearbySearchLiveData = SingleLiveEvent<Boolean>()
    val errorFetchingNearbySearchLiveData: LiveData<Boolean> = mutableErrorFetchingNearbySearchLiveData
    private val mutableNearbySearchResults = MutableSharedFlow<RepositoryResult<List<Restaurant>, Throwable?>>()
    private var favoritesFlow = MutableStateFlow<List<Restaurant>>(emptyList())

    val placesLiveData =
        combine(
            mutableSearchQuery.debounce(500),
            mutableNearbySearchResults,
            favoritesFlow.map { list -> list.map { restaurant -> restaurant.placeId } }) { query, nearbySearchResult, favoritePlaceIds ->
            var result: RepositoryResult<List<Restaurant>, Throwable?> = if (query.isEmpty()) {
                nearbySearchResult
            } else {
                placesRepository.getRestaurantsBySearchQuery(query)
            }
            if (result is Success<List<Restaurant>>) {
                val updatedList: List<Restaurant> = result.data.toMutableList().map {
                    it.copy(isFavorite = favoritePlaceIds.contains(it.placeId))
                }
                result = Success(updatedList)
            }
            result
        }.asLiveData()

    init {
        viewModelScope.launch {
            val lastKnownLocation = fusedLocationClient.lastLocation.suspended()
            if (lastKnownLocation != null) {
                mutableNearbySearchResults.emit(placesRepository.getNearbyRestaurants(lastKnownLocation))
            }
        }
        viewModelScope.launch {
            placesRepository.getFavoritesFlow().collect {
                favoritesFlow.emit(it)
            }
        }
    }

    fun onQueryTextChanged(newText: String) {
        viewModelScope.launch {
            mutableSearchQuery.emit(newText)
        }
    }

    fun goToMapView() {
        val value = placesLiveData.value
        val data = if (value is Success) {
            value.data
        } else null
        navigate(RestaurantListViewFragmentDirections.goToRestaurantMapViewFragment(MapListData(mutableSearchQuery.value, data)))
    }

    fun setMapListData(mapListData: MapListData) {
        viewModelScope.launch(dispatchers.default) {
            mutableSearchQuery.value = mapListData.query
            if (mapListData.resultList != null) {
                mutableNearbySearchResults.emit(Success(mapListData.resultList))
            }
        }
    }

    fun openDetailView(restaurant: Restaurant) {
        navigate(RestaurantListViewFragmentDirections.goToRestaurantDetailViewFragment(restaurant))
    }

    fun favoriteClicked(restaurant: Restaurant) {
        viewModelScope.launch(CoroutineName("favoriteClicked") + dispatchers.default) {
            placesRepository.toggleFavorite(restaurant)
        }
    }
}