package com.android.taller4.domain.usecase

import com.android.taller4.domain.repository.TransaccionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class Totales(
    val ingresos: Double,
    val gastos: Double,
    val balance: Double
)

class GetTotalesUseCase @Inject constructor(
    private val repository: TransaccionRepository
) {
    operator fun invoke(): Flow<Totales> {
        return combine(
            repository.obtenerTotalIngresos(),
            repository.obtenerTotalGastos()
        ) { ingresos, gastos ->
            Totales(
                ingresos = ingresos,
                gastos = gastos,
                balance = ingresos - gastos
            )
        }
    }
}