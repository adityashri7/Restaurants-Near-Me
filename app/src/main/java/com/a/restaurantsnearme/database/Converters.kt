package com.a.restaurantsnearme.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.a.restaurantsnearme.model.PlaceId

@TypeConverters
class Converters {
    @TypeConverter
    fun toPlaceId(id: String): PlaceId {
        return PlaceId(id)
    }

    @TypeConverter
    fun toString(placeId: PlaceId): String {
        return placeId.id
    }
}