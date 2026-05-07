package com.android.taller33.network

import com.android.taller33.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz de Retrofit para definir los puntos de enlace (endpoints) de la API.
 * Se han configurado los parámetros 'units' y 'lang' con valores por defecto
 * para asegurar que OpenWeatherMap devuelva los datos en sistema métrico (Celsius)
 * y en español.
 */
interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): WeatherResponse
}
