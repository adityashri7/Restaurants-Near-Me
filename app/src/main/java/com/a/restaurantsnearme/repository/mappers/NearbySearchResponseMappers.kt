package com.a.restaurantsnearme.repository.mappers

import com.a.restaurantsnearme.model.PlaceId
import com.a.restaurantsnearme.model.Restaurant
import com.a.restaurantsnearme.service.api.response.NearbySearchResponse

fun NearbySearchResponse.Place.toAppModel(): Restaurant {
    return Restaurant(PlaceId(place_id), name, vicinity, icon, rating, price_level, geometry.toAppModel())
}

private fun NearbySearchResponse.Geometry.toAppModel(): Restaurant.Geometry {
    return Restaurant.Geometry(location.toAppModel())
}

private fun NearbySearchResponse.Geometry.Location.toAppModel(): Restaurant.Geometry.Location {
    return Restaurant.Geometry.Location(lat, lng)
}