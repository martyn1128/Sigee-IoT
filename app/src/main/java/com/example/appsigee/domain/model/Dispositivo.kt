package com.example.appsigee.domain.model

// domain/model/Dispositivo.kt
data class Dispositivo(
    val id: String,
    val nombre: String,
    val tipo: String, // Puede ser el nombre del enum TipoDispositivo o una URI de imagen
    val costoSemanas: Double, // Ejemplo: 30.43
    val consumoKwh: Int,      // Ejemplo: 23
    val estaEnAlerta: Boolean = false, // Para pintar el borde rojo (ej. Refrigerador)
    val esBotonNuevo: Boolean = false,  // Para el botón de "+"
    val estado: Boolean = true
)

enum class TipoDispositivo {
    TELEVISION, CONSOLA, FOCO, REFRIGERADOR, MICROONDAS, LAPTOP, CELULAR, NUEVO
}
