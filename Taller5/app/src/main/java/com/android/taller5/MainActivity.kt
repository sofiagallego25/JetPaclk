package com.android.taller5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.taller5.presentation.screens.*
import com.android.taller5.ui.theme.Taller5Theme
import com.android.taller5.viewmodel.RecipeViewModel
import com.android.taller5.viewmodel.RecipeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Taller5Theme {
                val viewModel: RecipeViewModel = viewModel(
                    factory = RecipeViewModelFactory(application)
                )
                AppNavigation(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: RecipeViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToCamera = { navController.navigate("camera") },
                onNavigateToDetail = { id -> navController.navigate("detail/$id") }
            )
        }
        composable("camera") {
            CameraScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onIngredientsDetected = { navController.navigate("ingredients") }
            )
        }
        composable("ingredients") {
            IngredientListScreen(
                viewModel = viewModel,
                onNavigateToRecipes = { navController.navigate("recipes") }
            )
        }
        composable("recipes") {
            RecipeListScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onRecipeClick = { id -> navController.navigate("detail/$id") }
            )
        }
        composable(
            route = "detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            RecipeDetailScreen(
                recipeId = recipeId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
