package com.android.taller33.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.android.taller33.model.WeatherResponse
import com.android.taller33.viewmodel.WeatherViewModel

/**
 * WeatherScreen: Pantalla principal de la aplicación.
 * Compose no usa XML porque es declarativo: tú describes "qué" debe mostrarse
 * basándote en el estado, y Compose se encarga del "cómo" dibujarlo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    // collectAsState: Escucha los cambios en el StateFlow del ViewModel.
    // Cuando el estado cambia, esta función se vuelve a ejecutar (Recomposition).
    val uiState by viewModel.uiState.collectAsState()
    var cityInput by remember { mutableStateOf("") }

    // Colores dinámicos para el fondo (Extra: Gradiente)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App del Clima", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundGradient)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Buscador mejorado para soportar ciudad, país o región
            OutlinedTextField(
                value = cityInput,
                onValueChange = { cityInput = it },
                label = { Text("Ej: Pereira, CO", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ciudad o Ciudad, País", color = Color.White.copy(alpha = 0.5f)) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { if (cityInput.isNotBlank()) viewModel.getWeather(cityInput) }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // Ayuda visual para el usuario
            Text(
                text = "Puedes buscar por ciudad o ciudad + país (Ej: Pereira, CO)",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Manejo de Estados
            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is WeatherUiState.Success -> {
                    WeatherDetails(state.weather)
                }
                is WeatherUiState.Error -> {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherDetails(weather: WeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.cityName,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // Icono del clima usando Coil
            val iconUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@4x.png"
            AsyncImage(
                model = iconUrl,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "${weather.main.temp.toInt()}°C",
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = weather.weather[0].description.uppercase(),
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherExtraInfo(
                    icon = Icons.Default.WaterDrop,
                    label = "Humedad",
                    value = "${weather.main.humidity}%"
                )
                WeatherExtraInfo(
                    icon = Icons.Default.Air,
                    label = "Viento",
                    value = "${weather.wind.speed} m/s"
                )
            }
        }
    }
}

@Composable
fun WeatherExtraInfo(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        Text(text = value, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = label, fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f))
    }
}
