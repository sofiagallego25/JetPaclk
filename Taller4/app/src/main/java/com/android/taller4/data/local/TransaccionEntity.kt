package com.android.taller4.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.taller4.domain.model.TipoTransaccion
import com.android.taller4.domain.model.Transaccion

@Entity(tableName = "transacciones")
data class TransaccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,
    val monto: Double,
    val tipo: String, // "INGRESO" o "GASTO"
    val categoria: String,
    val fecha: Long
)

fun TransaccionEntity.toDomain(): Transaccion {
    return Transaccion(
        id = id,
        descripcion = descripcion,
        monto = monto,
        tipo = TipoTransaccion.valueOf(tipo),
        categoria = categoria,
        fecha = fecha
    )
}

fun Transaccion.toEntity(): TransaccionEntity {
    return TransaccionEntity(
        id = id,
        descripcion = descripcion,
        monto = monto,
        tipo = tipo.name,
        categoria = categoria,
        fecha = fecha
    )
}