package com.a.restaurantsnearme.injection

import android.content.Context
import androidx.room.Room
import com.a.restaurantsnearme.database.AppDatabase
import com.a.restaurantsnearme.repository.PlacesRepository
import com.a.restaurantsnearme.service.PlacesService
import com.a.restaurantsnearme.service.api.GooglePlacesApi
import com.a.restaurantsnearme.ui.home.HomeViewModelFactory
import com.a.restaurantsnearme.utils.DefaultDispatchers
import com.a.restaurantsnearme.utils.Dispatchers
import com.a.restaurantsnearme.utils.PermissionHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "database-name").build()

    @Provides
    fun provideHomeViewModelFactory(
        dispatchers: Dispatchers,
        placesRepository: PlacesRepository,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): HomeViewModelFactory =
        HomeViewModelFactory(dispatchers, placesRepository, fusedLocationProviderClient)

    @Provides
    fun provideDispatchers(): Dispatchers = DefaultDispatchers()

    @Provides
    fun providePermissionHelper(@ApplicationContext appContext: Context): PermissionHelper {
        return PermissionHelper(appContext)
    }

    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)

    @Provides
    fun providePlacesService(googlePlacesApi: GooglePlacesApi): PlacesService {
        return PlacesService(googlePlacesApi)
    }

    @Provides
    fun providePlacesRepository(placesService: PlacesService, appDatabase: AppDatabase, dispatchers: Dispatchers): PlacesRepository {
        return PlacesRepository(dispatchers, placesService, appDatabase.restaurantDao())
    }
}