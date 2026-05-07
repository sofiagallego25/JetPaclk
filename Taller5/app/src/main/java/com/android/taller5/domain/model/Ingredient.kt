package com.android.taller5.domain.model

data class Ingredient(
    val name: String,
    val confidence: Float = 1.0f,
    val isSelected: Boolean = true
)
