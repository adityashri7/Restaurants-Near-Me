package com.a.restaurantsnearme.repository.mappers

import com.a.restaurantsnearme.model.PlaceId
import com.a.restaurantsnearme.model.Restaurant
import com.a.restaurantsnearme.service.api.response.PlaceSearchResponse

fun PlaceSearchResponse.Place.toAppModel(): Restaurant {
    return Restaurant(PlaceId(place_id), name, formatted_address, icon, rating, price_level, geometry.toAppModel())
}

private fun PlaceSearchResponse.Geometry.toAppModel(): Restaurant.Geometry {
    return Restaurant.Geometry(location.toAppModel())
}

private fun PlaceSearchResponse.Geometry.Location.toAppModel(): Restaurant.Geometry.Location {
    return Restaurant.Geometry.Location(lat, lng)
}