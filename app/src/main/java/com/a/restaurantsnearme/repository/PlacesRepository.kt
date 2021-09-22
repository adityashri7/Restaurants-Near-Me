package com.a.restaurantsnearme.repository

import android.location.Location
import com.a.restaurantsnearme.database.RestaurantDao
import com.a.restaurantsnearme.model.Restaurant
import com.a.restaurantsnearme.repository.mappers.toAppModel
import com.a.restaurantsnearme.repository.mappers.toEntity
import com.a.restaurantsnearme.service.PlacesService
import com.a.restaurantsnearme.utils.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val dispatchers: Dispatchers,
    private val placesService: PlacesService,
    private val restaurantDao: RestaurantDao
) {

    suspend fun getFavoritesFlow() = withContext(dispatchers.default) {
        restaurantDao.getFavoritesFlow().map {
            it.toAppModel()
        }
    }

    suspend fun getRestaurantsBySearchQuery(query: String): RepositoryResult<List<Restaurant>, Throwable?> = withContext(dispatchers.default) {
        placesService.getPlacesByQuery(query).toRepositoryResult { placeSearchResponse ->
            placeSearchResponse.results.map { place ->
                val appModel = place.toAppModel()
                appModel
            }
        }
    }

    suspend fun getNearbyRestaurants(location: Location): RepositoryResult<List<Restaurant>, Throwable?> = withContext(dispatchers.default) {
        placesService.getNearbyPlaces(com.a.restaurantsnearme.service.api.request.Location("${location.latitude},${location.longitude}"))
            .toRepositoryResult { placeSearchResponse ->
                placeSearchResponse.results.map { place ->
                    val appModel = place.toAppModel()
                    appModel
                }
            }
    }

    suspend fun toggleFavorite(restaurant: Restaurant) = withContext(dispatchers.io) {
        if (restaurant.isFavorite) {
            val newRestaurant = restaurant.copy()
            newRestaurant.isFavorite = false
            restaurantDao.delete(newRestaurant.toEntity())
        } else {
            val newRestaurant = restaurant.copy()
            newRestaurant.isFavorite = true
            restaurantDao.insertAll(newRestaurant.toEntity())
        }
    }

}