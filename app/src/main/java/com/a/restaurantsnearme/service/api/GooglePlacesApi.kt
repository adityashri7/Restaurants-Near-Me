package com.a.restaurantsnearme.service.api

import com.a.restaurantsnearme.BuildConfig
import com.a.restaurantsnearme.service.api.response.NearbySearchResponse
import com.a.restaurantsnearme.service.api.response.PlaceSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    /**
     * gets Restaurants near user's location
     * @param location the users location formatted as latitude, longitude
     * @param radius the radius to search for
     * @param type the restaurant type by default
     * @param googleMapApiKey the map key to use for the api
     */
    @GET(ApiRoutes.GOOGLE_PLACE_NEARBY)
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int = 1500,
        @Query("type") type: String = "restaurant",
        @Query("key") googleMapApiKey: String = BuildConfig.PLACES_API_KEY
    ): NearbySearchResponse

    /**
     * gets Restaurants by search query
     * @param input the search query
     * @param type the restaurant type by default
     * @param location the users location formatted as latitude, longitude
     * @param googleMapApiKey the map key to use for the api
     */
    @GET(ApiRoutes.GOOGLE_PLACE_TEXT_SEARCH)
    suspend fun getPlacesByQuery(
        @Query("input") input: String?,
        @Query("type") type: String = "restaurant",
        @Query("location") location: String = "",
        @Query("key") googleMapApiKey: String = BuildConfig.PLACES_API_KEY
    ): PlaceSearchResponse
}