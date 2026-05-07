package com.android.taller5.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.taller5.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientListScreen(
    viewModel: RecipeViewModel,
    onNavigateToRecipes: () -> Unit
) {
    val ingredients by viewModel.detectedIngredients.collectAsState()
    var manualIngredient by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Ingredientes") })
        },
        floatingActionButton = {
            if (ingredients.any { it.isSelected }) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.generateAiRecipes()
                        onNavigateToRecipes()
                    },
                    icon = { Icon(Icons.Default.Check, contentDescription = "Generar") },
                    text = { Text("Generar Recetas") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Gestionar Ingredientes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Agrega manualmente o revisa los detectados.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Campo para agregar manualmente
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = manualIngredient,
                    onValueChange = { manualIngredient = it },
                    label = { Text("Nuevo ingrediente") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(
                    onClick = {
                        if (manualIngredient.isNotBlank()) {
                            viewModel.addManualIngredient(manualIngredient)
                            manualIngredient = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (ingredients.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("No hay ingredientes. ¡Agrega uno arriba!")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(ingredients) { ingredient ->
                        ListItem(
                            headlineContent = { 
                                Text(ingredient.name.replaceFirstChar { it.uppercase() }) 
                            },
                            leadingContent = {
                                Checkbox(
                                    checked = ingredient.isSelected,
                                    onCheckedChange = { viewModel.toggleIngredientSelection(ingredient.name) }
                                )
                            },
                            trailingContent = {
                                IconButton(onClick = { viewModel.removeIngredient(ingredient.name) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
