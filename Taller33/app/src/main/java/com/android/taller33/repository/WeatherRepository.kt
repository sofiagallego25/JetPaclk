package com.android.taller33.repository

import com.android.taller33.model.WeatherResponse
import com.android.taller33.network.RetrofitInstance

/**
 * Repositorio que maneja la lógica de obtención de datos.
 * MVVM: El Repositorio abstrae la fuente de datos del ViewModel.
 */
class WeatherRepository {
    private val api = RetrofitInstance.api
    private val apiKey = "Pon_Tu_API"

    suspend fun getWeather(city: String): WeatherResponse {
        return api.getWeather(city, apiKey)
    }
}
