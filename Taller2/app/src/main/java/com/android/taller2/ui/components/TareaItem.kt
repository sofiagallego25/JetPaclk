package com.android.taller2.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.taller2.model.Tarea
import com.android.taller2.ui.theme.Taller2Theme

/**
 * Componente reutilizable para mostrar una tarea individual.
 * @Composable: Indica que esta función define una interfaz de usuario en Compose.
 */
@Composable
fun TareaItem(
    tarea: Tarea,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    // animateColorAsState: Crea una transición suave de colores cuando cambia el estado
    val cardColor by animateColorAsState(
        if (tarea.completada) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.surface,
        label = "colorTransition"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = onCheckedChange
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (tarea.completada) Color.Gray else Color.Unspecified
                )
                Text(
                    text = tarea.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    color = if (tarea.completada) Color.Gray else Color.Unspecified
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TareaItemPreview() {
    Taller2Theme {
        TareaItem(
            tarea = Tarea(1, "Tarea de ejemplo", "Descripción corta"),
            onCheckedChange = {},
            onDelete = {},
            onClick = {}
        )
    }
}
