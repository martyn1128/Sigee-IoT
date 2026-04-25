package com.example.appsigee.domain.model

// Esta es la clase que agrupa los dispositivos por habitación
data class SeccionHabitacion(
    val nombre: String,           // Ejemplo: "Sala", "Cocina"
    val dispositivos: List<Dispositivo> // La lista de objetos tipo Dispositivo que creamos antes
)