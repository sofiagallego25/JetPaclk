package com.android.taller33.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para la respuesta de OpenWeatherMap.
 */
data class WeatherResponse(
    @SerializedName("name") val cityName: String,
    @SerializedName("main") val main: MainData,
    @SerializedName("weather") val weather: List<WeatherDescription>,
    @SerializedName("wind") val wind: WindData
)

data class MainData(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherDescription(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class WindData(
    @SerializedName("speed") val speed: Double
)
