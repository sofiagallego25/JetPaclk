package com.android.taller5.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.taller5.presentation.components.RecipeCard
import com.android.taller5.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: RecipeViewModel,
    onNavigateToCamera: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val favorites by viewModel.favoriteRecipes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asistente de Recetas IA", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCamera,
                icon = { Icon(Icons.Default.Add, contentDescription = "Nueva Receta") },
                text = { Text("Escanear Ingredientes") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Mis Recetas Favoritas",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.SemiBold
            )

            if (favorites.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No tienes recetas guardadas.\n¡Escanea ingredientes para empezar!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(favorites) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { onNavigateToDetail(recipe.id) },
                            onFavoriteToggle = { viewModel.deleteRecipe(recipe) }
                        )
                    }
                }
            }
        }
    }
}
