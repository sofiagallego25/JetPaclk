package com.android.taller5.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.android.taller5.data.local.AppDatabase
import com.android.taller5.domain.model.Ingredient
import com.android.taller5.domain.model.Recipe
import com.android.taller5.domain.service.AiRecipeService
import com.android.taller5.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository
    private val aiService = AiRecipeService()

    val favoriteRecipes: StateFlow<List<Recipe>>
    
    private val _detectedIngredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val detectedIngredients: StateFlow<List<Ingredient>> = _detectedIngredients.asStateFlow()

    private val _aiRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val aiRecipes: StateFlow<List<Recipe>> = _aiRecipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        val recipeDao = AppDatabase.getDatabase(application).recipeDao()
        repository = RecipeRepository(recipeDao)
        favoriteRecipes = repository.favoriteRecipes.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
        )
    }

    fun setDetectedIngredients(ingredients: List<String>) {
        val currentNames = _detectedIngredients.value.map { it.name.lowercase() }
        val newIngredients = ingredients
            .filter { it.lowercase() !in currentNames }
            .map { Ingredient(it.lowercase()) }
        
        _detectedIngredients.value = _detectedIngredients.value + newIngredients
    }

    fun addManualIngredient(name: String) {
        if (name.isBlank()) return
        val normalized = name.trim().lowercase()
        if (_detectedIngredients.value.any { it.name == normalized }) return
        
        _detectedIngredients.value = _detectedIngredients.value + Ingredient(normalized)
    }

    fun removeIngredient(name: String) {
        _detectedIngredients.value = _detectedIngredients.value.filter { it.name != name }
    }

    fun toggleIngredientSelection(name: String) {
        _detectedIngredients.value = _detectedIngredients.value.map {
            if (it.name == name) it.copy(isSelected = !it.isSelected) else it
        }
    }

    fun generateAiRecipes() {
        val selected = _detectedIngredients.value.filter { it.isSelected }.map { it.name }
        if (selected.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _aiRecipes.value = aiService.generateRecipes(selected)
            _isLoading.value = false
        }
    }

    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.insert(recipe.copy(isFavorite = true))
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.delete(recipe)
        }
    }
}

class RecipeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
