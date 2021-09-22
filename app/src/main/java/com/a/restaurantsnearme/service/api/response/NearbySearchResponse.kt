package com.a.restaurantsnearme.service.api.response

data class NearbySearchResponse(val results: List<Place>) {
    data class Place(
        val place_id: String,
        val name: String,
        val price_level: Int,
        val rating: Double,
        val icon: String,
        val vicinity: String,
        val geometry: Geometry
    )

    data class Geometry(val location: Location) {
        data class Location(val lat: Double, val lng: Double)
    }
}
