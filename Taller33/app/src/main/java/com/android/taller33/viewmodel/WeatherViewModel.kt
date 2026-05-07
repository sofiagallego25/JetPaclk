package com.android.taller33.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.taller33.repository.WeatherRepository
import com.android.taller33.ui.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel: Se encarga de la lógica de negocio y de mantener el estado de la UI.
 * Sobrevive a cambios de configuración (como rotar la pantalla).
 * 
 * StateFlow: Es un flujo de datos que emite el estado actual a la UI. 
 * Recomposition: Compose se redibuja automáticamente cuando el StateFlow cambia.
 */
class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()

    // Estado privado (Mutable)
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    // Estado público (Solo lectura para la UI)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        // Carga inicial por defecto
        getWeather("Pereira")
    }

    /**
     * Función para obtener el clima. Usa viewModelScope para lanzar una corrutina.
     */
    fun getWeather(city: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                Log.d("WeatherViewModel", "Consultando clima para: $city")
                val response = repository.getWeather(city)
                Log.d("WeatherViewModel", "Respuesta exitosa: $response")
                _uiState.value = WeatherUiState.Success(response)
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error al consultar: ${e.message}")
                _uiState.value = WeatherUiState.Error("No se pudo obtener el clima: ${e.message}")
            }
        }
    }
}
