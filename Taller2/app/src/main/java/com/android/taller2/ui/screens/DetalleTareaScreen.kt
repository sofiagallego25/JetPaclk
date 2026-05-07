package com.android.taller2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.taller2.model.Tarea
import com.android.taller2.ui.theme.Taller2Theme

/**
 * Pantalla de detalle que muestra la información completa de una tarea.
 * Se accede a través de la navegación pasando un ID.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTareaScreen(
    tarea: Tarea?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (tarea != null) {
                Icon(
                    imageVector = if (tarea.completada) Icons.Default.CheckCircle else Icons.Default.Pending,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = if (tarea.completada) Color(0xFF4CAF50) else Color(0xFFFFC107)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = tarea.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                AssistChip(
                    onClick = { },
                    label = { 
                        Text(if (tarea.completada) "Completada" else "Pendiente") 
                    },
                    leadingIcon = {
                        Icon(
                            if (tarea.completada) Icons.Default.CheckCircle else Icons.Default.Pending,
                            contentDescription = null,
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                )
            } else {
                Text("Tarea no encontrada")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleTareaPreview() {
    Taller2Theme {
        DetalleTareaScreen(
            tarea = Tarea(1, "Tarea de Prueba", "Esta es una descripción detallada"),
            onBack = {}
        )
    }
}
