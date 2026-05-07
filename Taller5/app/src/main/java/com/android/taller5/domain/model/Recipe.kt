package com.android.taller5.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val ingredients: String, // Comma separated
    val instructions: String,
    val difficulty: String,
    val calories: Int, // Cambiado a Int para cumplir requerimientos
    val isFavorite: Boolean = false
)
