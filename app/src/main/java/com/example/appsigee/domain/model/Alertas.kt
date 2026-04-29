package com.example.appsigee.domain.model

data class AlertaConsumo(
    val id: String,
    val tipo: String,
    val fechaHora: String,
    val estado: String // "ALERTA" o "OK"
)

data class ConfiguracionConsumo(
    val limiteKwh: Double,
    val tiempoMaximoHoras: Int,
    val apagarAutomaticamente: Boolean
)
