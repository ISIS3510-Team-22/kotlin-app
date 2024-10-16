package com.example.exchangeapp.model.service.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? {
        Log.d("PERMISSION", "Entro a getCurrentLocation()")
        return try {
            val locationResult = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, null // Usa alta precisión
            ).await() // Esperar la respuesta de ubicación

            // Verifica si locationResult es nulo
            if (locationResult != null) {
                Log.d("PERMISSION", "Ubicación obtenida: ${locationResult.latitude}, ${locationResult.longitude}")
            } else {
                Log.d("PERMISSION", "No se pudo obtener la ubicación: locationResult es nulo.")
            }

            locationResult

        } catch (e: Exception) {
            Log.e("LocationService", "Error getting location: ${e.message}")
            null
        }
    }
}