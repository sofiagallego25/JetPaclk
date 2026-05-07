package com.android.taller33

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.taller33.ui.WeatherScreen
import com.android.taller33.ui.theme.Taller33Theme

/**
 * MainActivity: Punto de entrada de la aplicación.
 * En Compose, no usamos setContentView(R.layout.activity_main).
 * En su lugar, usamos setContent { } para definir la UI con funciones Composable.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Habilita el diseño de borde a borde (edge-to-edge)
        enableEdgeToEdge()
        
        setContent {
            // Aplicamos el tema de la aplicación
            Taller33Theme {
                // Llamamos a nuestra pantalla principal del clima
                WeatherScreen()
            }
        }
    }
}
