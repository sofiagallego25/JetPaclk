package com.android.taller33.ui

import com.android.taller33.model.WeatherResponse

/**
 * Sealed class para representar los diferentes estados de la UI.
 * Un Sealed Class es como un Enum pero más potente, permitiendo pasar datos.
 */
sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
