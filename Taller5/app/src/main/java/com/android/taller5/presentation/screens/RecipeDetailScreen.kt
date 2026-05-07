package com.android.taller5.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.taller5.domain.model.Recipe
import com.android.taller5.presentation.components.TypewriterText
import com.android.taller5.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    viewModel: RecipeViewModel,
    onNavigateBack: () -> Unit
) {
    // Buscamos la receta en las listas actuales (IA o Favoritos)
    val aiRecipes by viewModel.aiRecipes.collectAsState()
    val favorites by viewModel.favoriteRecipes.collectAsState()
    
    val recipe = (aiRecipes + favorites).find { it.id == recipeId } ?: favorites.find { it.id == recipeId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle de Receta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    recipe?.let { r ->
                        IconButton(onClick = { viewModel.saveRecipe(r) }) {
                            Icon(
                                imageVector = if (r.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (r.isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        recipe?.let { r ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = r.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    SuggestionChip(
                        onClick = {}, 
                        label = { Text(text = r.difficulty.toString()) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    SuggestionChip(
                        onClick = {}, 
                        label = { Text(text = "${r.calories} kcal") }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Ingredientes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = r.ingredients,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Instrucciones de la IA",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                
                // Efecto Typewriter para simular IA
                TypewriterText(
                    text = r.instructions,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text(text = "Receta no encontrada")
        }
    }
}
