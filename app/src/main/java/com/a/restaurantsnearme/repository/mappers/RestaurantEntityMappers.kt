package com.a.restaurantsnearme.repository.mappers

import com.a.restaurantsnearme.model.Restaurant

fun List<com.a.restaurantsnearme.database.model.Restaurant>.toAppModel(): List<Restaurant> {
    return map { it.toAppModel() }
}

fun com.a.restaurantsnearme.database.model.Restaurant.toAppModel(): Restaurant {
    return Restaurant(placeId, name, formattedAddress, iconUrl, rating, priceLevel, Restaurant.Geometry(Restaurant.Geometry.Location(latitude, longitude)))
}

fun Restaurant.toEntity(): com.a.restaurantsnearme.database.model.Restaurant {
    return com.a.restaurantsnearme.database.model.Restaurant(
        placeId,
        name,
        formattedAddress,
        iconUrl,
        rating,
        priceLevel,
        geometry.location.lat,
        geometry.location.lng
    )
}