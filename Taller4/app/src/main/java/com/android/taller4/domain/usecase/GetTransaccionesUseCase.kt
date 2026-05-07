package com.android.taller4.domain.usecase

import com.android.taller4.domain.model.Transaccion
import com.android.taller4.domain.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransaccionesUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    operator fun invoke(): Flow<List<Transaccion>> = repository.obtenerTransacciones()
}