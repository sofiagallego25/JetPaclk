package com.android.taller4.di

import android.content.Context
import androidx.room.Room
import com.android.taller4.data.local.AppDatabase
import com.android.taller4.data.local.TransaccionDao
import com.android.taller4.data.repository.TransaccionRepositoryImpl
import com.android.taller4.domain.repository.TransaccionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "finanzas_db"
        ).build()
    }

    @Provides
    fun provideTransaccionDao(db: AppDatabase): TransaccionDao {
        return db.transaccionDao
    }

    @Provides
    @Singleton
    fun provideTransaccionRepository(dao: TransaccionDao): TransaccionRepository {
        return TransaccionRepositoryImpl(dao)
    }
}