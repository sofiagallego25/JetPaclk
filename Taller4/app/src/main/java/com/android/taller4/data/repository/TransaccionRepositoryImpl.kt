package com.android.taller4.data.repository

import com.android.taller4.data.local.TransaccionDao
import com.android.taller4.data.local.toDomain
import com.android.taller4.data.local.toEntity
import com.android.taller4.domain.model.Transaccion
import com.android.taller4.domain.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransaccionRepositoryImpl @Inject constructor(
    private val dao: TransaccionDao
) : TransaccionRepository {

    override fun obtenerTransacciones(): Flow<List<Transaccion>> {
        return dao.obtenerTodo().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertarTransaccion(transaccion: Transaccion) {
        dao.insertar(transaccion.toEntity())
    }

    override suspend fun eliminarTransaccion(transaccion: Transaccion) {
        dao.eliminar(transaccion.toEntity())
    }

    override fun obtenerTotalIngresos(): Flow<Double> {
        return dao.obtenerTotalIngresos().map { it ?: 0.0 }
    }

    override fun obtenerTotalGastos(): Flow<Double> {
        return dao.obtenerTotalGastos().map { it ?: 0.0 }
    }
}