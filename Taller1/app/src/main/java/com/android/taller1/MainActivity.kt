package com.android.taller1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.taller1.ui.theme.Taller1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge: Permite que la aplicación utilice todo el espacio de la pantalla.
        enableEdgeToEdge()
        
        setContent {
            // Taller1Theme: Aplica el tema Material3 del proyecto.
            Taller1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TarjetaBienvenida()
                        PantallaPresentacion()
                    }
                }
            }
        }
    }
}

/**
 * @Composable: Indica que esta función es un componente de UI. Compose construye la interfaz
 * mediante funciones de Kotlin en lugar de archivos XML.
 *
 * ¿Por qué no XML? Al usar solo Kotlin, el código es más fácil de mantener, evitamos errores
 * de "findViewById" y la interfaz reacciona automáticamente a los cambios de estado.
 */
@Composable
fun TarjetaBienvenida() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        // Modifier: Se usa para ajustar el diseño (padding, tamaño, alineación, etc.)
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "¡Hola, Sofia!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bienvenida a Jetpack Compose",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun PantallaPresentacion() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar circular
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.padding(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sofia",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Estudiante de Programación Móvil",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Desarrolladora enfocada en crear interfaces modernas y declarativas siguiendo los principios de Material3.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Acción */ }) {
                    Icon(Icons.Default.Email, contentDescription = "Correo")
                }
                IconButton(onClick = { /* Acción */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Redes")
                }
                IconButton(onClick = { /* Acción */ }) {
                    Icon(Icons.Default.Person, contentDescription = "Perfil")
                }
            }
        }
    }
}

/**
 * @Preview: Permite ver el diseño directamente en el IDE sin necesidad de un emulador.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTaller1() {
    Taller1Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaBienvenida()
            PantallaPresentacion()
        }
    }
}
