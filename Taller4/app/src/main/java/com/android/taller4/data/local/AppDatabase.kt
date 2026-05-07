package com.android.taller4.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TransaccionEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val transaccionDao: TransaccionDao
}