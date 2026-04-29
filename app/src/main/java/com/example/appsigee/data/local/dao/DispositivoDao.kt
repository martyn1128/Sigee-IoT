package com.example.appsigee.data.local.dao

import androidx.room.*
import com.example.appsigee.data.local.entity.DispositivoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DispositivoDao {
    @Query("SELECT * FROM dispositivos")
    fun getAllDispositivos(): Flow<List<DispositivoEntity>>

    @Query("SELECT * FROM dispositivos WHERE id_grupo = :grupoId")
    fun getDispositivosByGrupo(grupoId: String): Flow<List<DispositivoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDispositivo(dispositivo: DispositivoEntity)

    @Update
    suspend fun updateDispositivo(dispositivo: DispositivoEntity)

    @Delete
    suspend fun deleteDispositivo(dispositivo: DispositivoEntity)

    @Query("SELECT * FROM dispositivos WHERE id_dispositivo = :id")
    fun getDispositivoById(id: String): Flow<DispositivoEntity?>
}
