package com.a.restaurantsnearme.ui.home

import com.a.restaurantsnearme.model.Restaurant
import java.io.Serializable

data class MapListData(val query: String = "", val resultList: List<Restaurant>? = null) : Serializable