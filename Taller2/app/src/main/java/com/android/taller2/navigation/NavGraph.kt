package com.android.taller2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.android.taller2.model.Tarea
import com.android.taller2.ui.screens.DetalleTareaScreen
import com.android.taller2.ui.screens.ListaTareasScreen

/**
 * NavController: Objeto que gestiona la navegación entre pantallas.
 * NavHost: Contenedor que muestra la pantalla actual basada en la ruta.
 */
@Composable
fun SetupNavGraph(navController: NavHostController) {
    // Estado global de las tareas (simulado para el taller)
    // mutableStateListOf: Permite que Compose detecte cambios en la lista (add/remove)
    val tareas = remember {
        mutableListOf(
            Tarea(1, "Aprender Compose", "Estudiar modificadores y estados"),
            Tarea(2, "Configurar Navegación", "Implementar NavHost y NavController"),
            Tarea(3, "Diseñar UI", "Usar Material3 y LazyColumn")
        ).toMutableStateList()
    }

    NavHost(
        navController = navController,
        startDestination = "lista"
    ) {
        // Ruta de la lista de tareas
        composable("lista") {
            ListaTareasScreen(
                tareas = tareas,
                onNavigateToDetail = { id ->
                    navController.navigate("detalle/$id")
                }
            )
        }
        
        // Ruta de detalle con argumento ID
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val tarea = tareas.find { it.id == id }
            
            DetalleTareaScreen(
                tarea = tarea,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
