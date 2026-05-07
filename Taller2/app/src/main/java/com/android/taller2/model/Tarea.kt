package com.android.taller2.model

/**
 * Data class que representa una Tarea.
 * En Compose, las data classes son ideales para representar el estado de la UI.
 */
data class Tarea(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    var completada: Boolean = false
)
