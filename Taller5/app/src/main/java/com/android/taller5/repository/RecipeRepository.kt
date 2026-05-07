package com.android.taller5.repository

import com.android.taller5.data.local.RecipeDao
import com.android.taller5.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {
    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()
    val favoriteRecipes: Flow<List<Recipe>> = recipeDao.getFavoriteRecipes()

    suspend fun insert(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }
}
