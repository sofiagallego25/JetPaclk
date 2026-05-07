package com.android.taller4.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun obtenerTodo(): Flow<List<TransaccionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(transaccion: TransaccionEntity)

    @Delete
    suspend fun eliminar(transaccion: TransaccionEntity)

    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'INGRESO'")
    fun obtenerTotalIngresos(): Flow<Double?>

    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'GASTO'")
    fun obtenerTotalGastos(): Flow<Double?>
}