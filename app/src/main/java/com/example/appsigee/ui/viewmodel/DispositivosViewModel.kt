package com.example.appsigee.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsigee.data.local.dao.GrupoDao
import com.example.appsigee.data.repository.DispositivoRepository
import com.example.appsigee.domain.model.SeccionHabitacion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class DispositivosViewModel(
    private val repository: DispositivoRepository,
    private val grupoDao: GrupoDao
) : ViewModel() {

    private val _habitaciones = MutableStateFlow<List<SeccionHabitacion>>(emptyList())
    val habitaciones: StateFlow<List<SeccionHabitacion>> = _habitaciones.asStateFlow()

    private val _grupos = MutableStateFlow<List<com.example.appsigee.data.local.entity.GrupoEntity>>(emptyList())
    val grupos: StateFlow<List<com.example.appsigee.data.local.entity.GrupoEntity>> = _grupos.asStateFlow()

    init {
        observeData()
        observeGrupos()
    }

    private fun observeGrupos() {
        viewModelScope.launch {
            grupoDao.getAllGrupos().collect {
                _grupos.value = it
            }
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            // Usamos flatMapLatest para que cada vez que la lista de grupos cambie,
            // se genere un nuevo Flow combinado de todas las secciones.
            grupoDao.getAllGrupos()
                .flatMapLatest { grupos ->
                    if (grupos.isEmpty()) {
                        flowOf(emptyList<SeccionHabitacion>())
                    } else {
                        val flowsDeSecciones = grupos.map { grupo ->
                            repository.getDispositivosByGrupo(grupo.id_grupo).map { dispositivos ->
                                SeccionHabitacion(
                                    id_grupo = grupo.id_grupo,
                                    nombre = grupo.nombre,
                                    dispositivos = dispositivos
                                )
                            }
                        }
                        combine(flowsDeSecciones) { it.toList() }
                    }
                }
                .collect { listaSecciones ->
                    _habitaciones.value = listaSecciones
                }
        }
    }

    fun updateDispositivo(id: String, nombre: String, grupoId: String, tipo: String) {
        viewModelScope.launch {
            // repository.updateDispositivo(id, nombre, grupoId, tipo)
        }
    }

    fun updateGrupoNombre(id: String, nuevoNombre: String) {
        viewModelScope.launch {
            val grupoActual = grupoDao.getGrupoById(id)
            grupoActual?.let {
                grupoDao.updateGrupo(it.copy(nombre = nuevoNombre))
            }
        }
    }

    fun getDispositivoById(id: String): Flow<com.example.appsigee.domain.model.Dispositivo?> {
        return repository.getDispositivoById(id)
    }

    fun toggleDispositivoEstado(dispositivo: com.example.appsigee.domain.model.Dispositivo) {
        viewModelScope.launch {
            // Obtenemos el grupoId actual para el update
            // En una implementación real, el Dispositivo podría tener el id_grupo
            // Por ahora, buscamos en qué habitación está
            val grupoId = _habitaciones.value.find { h -> 
                h.dispositivos.any { d -> d.id == dispositivo.id } 
            }?.id_grupo ?: ""

            val updatedEntity = com.example.appsigee.data.local.entity.DispositivoEntity(
                id_dispositivo = dispositivo.id,
                nombre = dispositivo.nombre,
                tipo = dispositivo.tipo.name,
                estado = !dispositivo.estado,
                consumo_actual = dispositivo.consumoKwh.toDouble(),
                id_gateway = null,
                id_grupo = grupoId
            )
            repository.update(updatedEntity)
        }
    }
}
