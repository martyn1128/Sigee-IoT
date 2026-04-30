package com.example.appsigee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appsigee.data.local.entity.ConfiguracionConsumoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfiguracionConsumoDao {
    @Query("SELECT * FROM configuracion_consumo WHERE id_dispositivo = :idDispositivo LIMIT 1")
    fun getConfiguracionByDispositivo(idDispositivo: String): Flow<ConfiguracionConsumoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateConfiguracion(configuracion: ConfiguracionConsumoEntity)

    @Query("DELETE FROM configuracion_consumo WHERE id_dispositivo = :idDispositivo")
    suspend fun deleteConfiguracionByDispositivo(idDispositivo: String)
}
