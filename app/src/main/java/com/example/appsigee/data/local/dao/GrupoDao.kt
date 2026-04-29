package com.example.appsigee.data.local.dao

import androidx.room.*
import com.example.appsigee.data.local.entity.GrupoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {
    @Query("SELECT * FROM grupos")
    fun getAllGrupos(): Flow<List<GrupoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrupo(grupo: GrupoEntity)

    @Update
    suspend fun updateGrupo(grupo: GrupoEntity)

    @Query("SELECT * FROM grupos WHERE id_grupo = :id LIMIT 1")
    suspend fun getGrupoById(id: String): GrupoEntity?
}
