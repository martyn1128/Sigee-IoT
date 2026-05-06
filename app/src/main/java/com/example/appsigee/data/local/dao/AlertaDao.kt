package com.example.appsigee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsigee.data.local.entity.AlertaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertaDao {
    @Query("SELECT * FROM alertas WHERE id_dispositivo = :idDispositivo ORDER BY fecha DESC")
    fun getAlertasByDispositivo(idDispositivo: String): Flow<List<AlertaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerta(alerta: AlertaEntity)
}
