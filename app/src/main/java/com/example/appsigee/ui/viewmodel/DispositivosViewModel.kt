package com.example.appsigee.ui.viewmodel// ui/viewmodel/DispositivosViewModel.kt
import androidx.lifecycle.ViewModel
import com.example.appsigee.domain.model.Dispositivo
import com.example.appsigee.domain.model.TipoDispositivo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.appsigee.domain.model.SeccionHabitacion

// Estructura para organizar por sala
class DispositivosViewModel : ViewModel() {

    // Estado que la UI "observará"
    private val _habitaciones = MutableStateFlow<List<SeccionHabitacion>>(emptyList())
    val habitaciones: StateFlow<List<SeccionHabitacion>> = _habitaciones.asStateFlow()

    init {
        // En una app real, esto vendría de un repositorio
        cargarDatosDePrueba()
    }

    private fun cargarDatosDePrueba() {
        val sala = SeccionHabitacion(
            nombre = "Sala",
            dispositivos = listOf(
                Dispositivo("1", "Televisión", TipoDispositivo.TELEVISION, 30.43, 23),
                Dispositivo("2", "Nintendo", TipoDispositivo.CONSOLA, 20.21, 15),
                Dispositivo("3", "Foco1", TipoDispositivo.FOCO, 6.18, 5)
            )
        )
        val cocina = SeccionHabitacion(
            nombre = "Cocina",
            dispositivos = listOf(
                Dispositivo(
                    "4",
                    "Refrigerador",
                    TipoDispositivo.REFRIGERADOR,
                    80.01,
                    91,
                    estaEnAlerta = true
                ),
                Dispositivo("5", "Microondas", TipoDispositivo.MICROONDAS, 40.86, 38),
                Dispositivo(
                    "6",
                    "NuevoDispositivo",
                    TipoDispositivo.NUEVO,
                    0.0,
                    0,
                    esBotonNuevo = true
                )
            )
        )
        // Añade la tercera habitación...

        _habitaciones.value = listOf(sala, cocina /*, habitacionJuan */)
    }
}