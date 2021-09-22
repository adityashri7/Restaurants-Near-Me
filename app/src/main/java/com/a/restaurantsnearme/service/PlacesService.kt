package com.a.restaurantsnearme.service

import com.a.restaurantsnearme.service.api.GooglePlacesApi
import com.a.restaurantsnearme.service.api.request.Location
import com.a.restaurantsnearme.service.api.response.NearbySearchResponse
import com.a.restaurantsnearme.service.api.response.PlaceSearchResponse
import javax.inject.Inject


class PlacesService @Inject constructor(private val googlePlacesApi: GooglePlacesApi) {
    suspend fun getPlacesByQuery(query: String): ServiceResult<PlaceSearchResponse, Throwable?> {
        return runAuthenticatedRemoteCatching {
            googlePlacesApi.getPlacesByQuery(query)
        }
    }

    suspend fun getNearbyPlaces(location: Location): ServiceResult<NearbySearchResponse, Throwable?> {
        return runAuthenticatedRemoteCatching {
            googlePlacesApi.getNearbyPlaces(location.location)
        }
    }
}