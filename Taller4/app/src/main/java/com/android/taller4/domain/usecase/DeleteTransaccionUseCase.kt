package com.android.taller4.domain.usecase

import com.android.taller4.domain.model.Transaccion
import com.android.taller4.domain.repository.TransaccionRepository
import javax.inject.Inject

class DeleteTransaccionUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    suspend operator fun invoke(transaccion: Transaccion) = repository.eliminarTransaccion(transaccion)
}