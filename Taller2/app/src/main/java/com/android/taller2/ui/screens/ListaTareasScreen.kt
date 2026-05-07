package com.android.taller2.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.taller2.model.Tarea
import com.android.taller2.ui.components.TareaItem
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.android.taller2.ui.theme.Taller2Theme
import kotlinx.coroutines.launch

/**
 * Pantalla principal que muestra la lista de tareas.
 * 
 * CONCEPTOS CLAVE:
 * - @Composable: Indica que la función define un bloque de UI reactiva.
 * - Recomposition: Proceso de actualizar la UI cuando cambian los datos.
 * - LazyColumn: Equivalente a RecyclerView, solo carga lo que se ve en pantalla.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTareasScreen(
    tareas: SnapshotStateList<Tarea>,
    onNavigateToDetail: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Tareas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Tarea")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (tareas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay tareas. ¡Agrega una!", style = MaterialTheme.typography.bodyLarge)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
            ) {
                items(tareas, key = { it.id }) { tarea ->
                    // Swipe to Delete moderno con SwipeToDismissBox
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) {
                                val index = tareas.indexOf(tarea)
                                tareas.remove(tarea)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Tarea eliminada",
                                        actionLabel = "Deshacer",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        tareas.add(index, tarea)
                                    }
                                }
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                MaterialTheme.colorScheme.errorContainer
                            } else Color.Transparent
                            
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.onErrorContainer)
                            }
                        },
                        enableDismissFromStartToEnd = false
                    ) {
                        TareaItem(
                            tarea = tarea,
                            onCheckedChange = { isChecked ->
                                val index = tareas.indexOf(tarea)
                                if (index != -1) {
                                    tareas[index] = tarea.copy(completada = isChecked)
                                }
                            },
                            onDelete = {
                                tareas.remove(tarea)
                            },
                            onClick = { onNavigateToDetail(tarea.id) }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AgregarTareaDialog(
                onDismiss = { showDialog = false },
                onConfirm = { titulo, desc ->
                    val nuevaTarea = Tarea(
                        id = (tareas.maxOfOrNull { it.id } ?: 0) + 1,
                        titulo = titulo,
                        descripcion = desc
                    )
                    tareas.add(nuevaTarea)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AgregarTareaDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    // remember: Mantiene el valor del estado durante la recomposición
    // mutableStateOf: Notifica a Compose que el valor cambió y debe redibujar
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { 
                        titulo = it
                        if (it.isNotBlank()) error = false
                    },
                    label = { Text("Título *") },
                    isError = error,
                    supportingText = { if (error) Text("Campo obligatorio") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (titulo.isNotBlank()) {
                    onConfirm(titulo, descripcion)
                } else {
                    error = true
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ListaTareasPreview() {
    Taller2Theme {
        val tareasMock = remember {
            mutableListOf(
                Tarea(1, "Hacer Taller 2", "Completar la app de tareas", false),
                Tarea(2, "Estudiar Jetpack", "Aprender sobre Navigation", true)
            ).toMutableStateList()
        }
        ListaTareasScreen(tareas = tareasMock, onNavigateToDetail = {})
    }
}
