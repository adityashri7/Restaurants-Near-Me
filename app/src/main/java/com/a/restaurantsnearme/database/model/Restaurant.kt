package com.a.restaurantsnearme.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.a.restaurantsnearme.model.PlaceId

@Entity(indices = [Index(value = ["placeId"], unique = true)])
data class Restaurant(
    @PrimaryKey val placeId: PlaceId,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "formattedAddress") val formattedAddress: String?,
    @ColumnInfo(name = "iconUrl") val iconUrl: String,
    @ColumnInfo(name = "rating") val rating: Double,
    @ColumnInfo(name = "priceLevel") val priceLevel: Int,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
)