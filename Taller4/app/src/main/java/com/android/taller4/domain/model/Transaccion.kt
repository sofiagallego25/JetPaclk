package com.android.taller4.domain.model

import java.util.Date

data class Transaccion(
    val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: TipoTransaccion,
    val categoria: String,
    val fecha: Long = System.currentTimeMillis()
)

enum class TipoTransaccion {
    INGRESO, GASTO
}