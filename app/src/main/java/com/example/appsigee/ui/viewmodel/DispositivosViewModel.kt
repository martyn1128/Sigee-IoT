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

    init {
        observeData()
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
}
