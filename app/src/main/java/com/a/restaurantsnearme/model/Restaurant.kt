package com.a.restaurantsnearme.model

import java.io.Serializable

data class Restaurant(
    val placeId: PlaceId,
    val name: String?,
    val formattedAddress: String?,
    val iconUrl: String,
    val rating: Double,
    val priceLevel: Int,
    val geometry: Geometry,
    var isFavorite: Boolean = false,
) : Serializable {
    data class Geometry(val location: Location) : Serializable {
        data class Location(val lat: Double, val lng: Double) : Serializable
    }
}
