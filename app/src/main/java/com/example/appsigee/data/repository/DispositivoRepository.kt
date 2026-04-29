package com.example.appsigee.data.repository

import com.example.appsigee.data.local.dao.DispositivoDao
import com.example.appsigee.data.local.entity.DispositivoEntity
import com.example.appsigee.domain.model.Dispositivo
import com.example.appsigee.domain.model.TipoDispositivo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DispositivoRepository(private val dispositivoDao: DispositivoDao) {

    val allDispositivos: Flow<List<Dispositivo>> = dispositivoDao.getAllDispositivos().map { entities ->
        entities.map { it.toDomain() }
    }

    fun getDispositivosByGrupo(grupoId: String): Flow<List<Dispositivo>> {
        return dispositivoDao.getDispositivosByGrupo(grupoId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insert(dispositivo: Dispositivo, grupoId: String) {
        dispositivoDao.insertDispositivo(dispositivo.toEntity(grupoId))
    }
    suspend fun update(dispositivo: DispositivoEntity) {
        dispositivoDao.updateDispositivo(dispositivo)
    }

    // Mappers
    private fun DispositivoEntity.toDomain(): Dispositivo {
        return Dispositivo(
            id = id_dispositivo,
            nombre = nombre,
            tipo = try { TipoDispositivo.valueOf(tipo) } catch (e: Exception) { TipoDispositivo.NUEVO },
            costoSemanas = 0.0, // Estos datos podrían venir de otras tablas (Consumo) en una implementación completa
            consumoKwh = consumo_actual.toInt(),
            estaEnAlerta = false, // Podría venir de la tabla Alertas
            esBotonNuevo = (tipo == TipoDispositivo.NUEVO.name)
        )
    }

    private fun Dispositivo.toEntity(grupoId: String): DispositivoEntity {
        return DispositivoEntity(
            id_dispositivo = id,
            nombre = nombre,
            tipo = tipo.name,
            estado = true, // Valor por defecto
            consumo_actual = consumoKwh.toDouble(),
            id_gateway = null,
            id_grupo = grupoId
        )
    }
}
