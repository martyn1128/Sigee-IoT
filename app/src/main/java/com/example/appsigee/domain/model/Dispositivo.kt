package com.example.appsigee.domain.model

// domain/model/Dispositivo.kt (o simplemente crea una carpeta model si estás empezando)
data class Dispositivo(
    val id: String,
    val nombre: String,
    val tipo: TipoDispositivo, // Una imagen, icono o ID de recurso
    val costoSemanas: Double, // Ejemplo: 30.43
    val consumoKwh: Int,      // Ejemplo: 23
    val estaEnAlerta: Boolean = false, // Para pintar el borde rojo (ej. Refrigerador)
    val esBotonNuevo: Boolean = false  // Para el botón de "+"
)

enum class TipoDispositivo {
    TELEVISION, CONSOLA, FOCO, REFRIGERADOR, MICROONDAS, LAPTOP, CELULAR, NUEVO
}