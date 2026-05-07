package com.android.taller2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.android.taller2.navigation.SetupNavGraph
import com.android.taller2.ui.theme.Taller2Theme

/**
 * MainActivity: Punto de entrada de la aplicación.
 * 
 * En Jetpack Compose, la Activity actúa como el contenedor principal (Host).
 * Ya no usamos setContentView(R.layout.activity_main) porque la UI se define
 * mediante funciones @Composable dentro de setContent { ... }.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Habilita el diseño de borde a borde (Edge-to-Edge)
        enableEdgeToEdge()
        
        setContent {
            // Taller2Theme: Aplica los colores, tipografía y formas de Material3
            Taller2Theme {
                // Surface: Componente básico que proporciona el fondo correcto según el tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // rememberNavController: Crea y recuerda el controlador de navegación
                    // que permite movernos entre pantallas sin perder el estado.
                    val navController = rememberNavController()
                    
                    // SetupNavGraph: Función que define nuestras rutas y destinos
                    SetupNavGraph(navController = navController)
                }
            }
        }
    }
}
