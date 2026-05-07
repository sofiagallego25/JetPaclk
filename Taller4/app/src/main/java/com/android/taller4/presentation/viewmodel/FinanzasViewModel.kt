package com.android.taller4.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.taller4.domain.model.TipoTransaccion
import com.android.taller4.domain.model.Transaccion
import com.android.taller4.domain.usecase.AddTransaccionUseCase
import com.android.taller4.domain.usecase.DeleteTransaccionUseCase
import com.android.taller4.domain.usecase.GetTotalesUseCase
import com.android.taller4.domain.usecase.GetTransaccionesUseCase
import com.android.taller4.domain.usecase.Totales
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanzasViewModel @Inject constructor(
    private val getTransaccionesUseCase: GetTransaccionesUseCase,
    private val addTransaccionUseCase: AddTransaccionUseCase,
    private val deleteTransaccionUseCase: DeleteTransaccionUseCase,
    private val getTotalesUseCase: GetTotalesUseCase
) : ViewModel() {

    val transacciones: StateFlow<List<Transaccion>> = getTransaccionesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totales: StateFlow<Totales> = getTotalesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Totales(0.0, 0.0, 0.0))

    fun agregarTransaccion(descripcion: String, monto: Double, tipo: TipoTransaccion, categoria: String) {
        viewModelScope.launch {
            val nuevaTransaccion = Transaccion(
                descripcion = descripcion,
                monto = monto,
                tipo = tipo,
                categoria = categoria
            )
            addTransaccionUseCase(nuevaTransaccion)
        }
    }

    fun eliminarTransaccion(transaccion: Transaccion) {
        viewModelScope.launch {
            deleteTransaccionUseCase(transaccion)
        }
    }
}