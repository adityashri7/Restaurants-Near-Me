package com.a.restaurantsnearme.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.a.restaurantsnearme.database.model.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurant")
    fun getFavoritesFlow(): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurant")
    fun getFavorites(): List<Restaurant>

    @Insert
    fun insertAll(vararg arrayOfRestaurants: Restaurant)

    @Delete
    fun delete(restaurant: Restaurant)
}