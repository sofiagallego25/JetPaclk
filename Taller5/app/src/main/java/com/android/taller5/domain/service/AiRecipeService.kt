package com.android.taller5.domain.service

import android.util.Log
import com.android.taller5.domain.model.Recipe
import com.android.taller5.network.ChatRequest
import com.android.taller5.network.Constants
import com.android.taller5.network.Message
import com.android.taller5.network.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class AiRecipeService {
    suspend fun generateRecipes(ingredients: List<String>): List<Recipe> {
        return try {
            val ingredientsList = ingredients.joinToString(", ")
            val prompt = """
                Genera una receta creativa usando estos ingredientes: $ingredientsList.
                Responde ÚNICAMENTE en este formato exacto:
                Título: [Nombre]
                Dificultad: [Baja/Media/Alta]
                Calorías: [Solo el número]
                Instrucciones: [Pasos]
            """.trimIndent()

            val request = ChatRequest(
                messages = listOf(
                    Message(role = "system", content = "Eres un chef experto."),
                    Message(role = "user", content = prompt)
                )
            )

            // LOG: Antes de enviar
            Log.d("API_DEBUG", "--- INICIO PETICIÓN OPENAI ---")
            Log.d("API_DEBUG", "URL: ${Constants.BASE_URL}chat/completions")
            Log.d("API_DEBUG", "Payload: $request")

            val response = RetrofitClient.openAiApiService.getRecipeRecommendation(
                authorization = "Bearer ${Constants.OPENAI_API_KEY}",
                request = request
            )

            // LOG: Respuesta exitosa
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            Log.d("API_DEBUG", "Respuesta exitosa: $content")
            
            parseAiResponse(content, ingredientsList)

        } catch (e: HttpException) {
            val code = e.code()
            val errorBody = e.response()?.errorBody()?.string()
            
            // LOG: Error HTTP específico (401, 429, 500, etc.)
            Log.e("API_ERROR", "Error HTTP $code")
            Log.e("API_ERROR", "Cuerpo del error: $errorBody")
            
            val mensajeUI = when(code) {
                401 -> "API Key inválida o no autorizada (401)."
                429 -> "Cuota excedida o demasiadas peticiones (429)."
                else -> "Error del servidor OpenAI ($code)."
            }

            listOf(Recipe(
                title = "Error de IA",
                ingredients = ingredients.joinToString(", "),
                instructions = "$mensajeUI\n\nDetalle: $errorBody",
                difficulty = "N/A",
                calories = 0
            ))
        } catch (e: IOException) {
            Log.e("API_ERROR", "Error de red (sin internet o timeout)", e)
            listOf(Recipe(
                title = "Error de Conexión",
                ingredients = ingredients.joinToString(", "),
                instructions = "No se pudo conectar al servidor. Revisa tu conexión a Internet.",
                difficulty = "Red",
                calories = 0
            ))
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error inesperado", e)
            listOf(Recipe(
                title = "Error Inesperado",
                ingredients = ingredients.joinToString(", "),
                instructions = "Ocurrió un error: ${e.message}",
                difficulty = "Error",
                calories = 0
            ))
        }
    }

    private fun parseAiResponse(content: String, ingredients: String): List<Recipe> {
        return try {
            val lines = content.lines()
            val title = lines.find { it.startsWith("Título:") }?.removePrefix("Título:")?.trim() ?: "Receta IA"
            val difficulty = lines.find { it.startsWith("Dificultad:") }?.removePrefix("Dificultad:")?.trim() ?: "Media"
            val caloriesText = lines.find { it.startsWith("Calorías:") }?.removePrefix("Calorías:")?.trim() ?: "0"
            val caloriesInt = caloriesText.filter { it.isDigit() }.toIntOrNull() ?: 0
            val instructions = content.substringAfter("Instrucciones:").trim()

            listOf(
                Recipe(
                    title = title,
                    ingredients = ingredients,
                    instructions = instructions,
                    difficulty = difficulty,
                    calories = caloriesInt
                )
            )
        } catch (e: Exception) {
            listOf(
                Recipe(
                    title = "Receta Generada",
                    ingredients = ingredients,
                    instructions = content,
                    difficulty = "Media",
                    calories = 0
                )
            )
        }
    }
}
