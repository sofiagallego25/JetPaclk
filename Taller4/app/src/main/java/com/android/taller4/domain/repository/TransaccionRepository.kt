package com.android.taller4.domain.repository

import com.android.taller4.domain.model.Transaccion
import kotlinx.coroutines.flow.Flow

interface TransaccionRepository {
    fun obtenerTransacciones(): Flow<List<Transaccion>>
    suspend fun insertarTransaccion(transaccion: Transaccion)
    suspend fun eliminarTransaccion(transaccion: Transaccion)
    fun obtenerTotalIngresos(): Flow<Double>
    fun obtenerTotalGastos(): Flow<Double>
}