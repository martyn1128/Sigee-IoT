package com.example.appsigee.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id_usuario: String,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val num_serv: String
)

@Entity(
    tableName = "grupos",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GrupoEntity(
    @PrimaryKey val id_grupo: String,
    val nombre: String,
    val id_usuario: String
)

@Entity(tableName = "gateways")
data class GatewayEntity(
    @PrimaryKey val id_gateway: String,
    val nombre: String,
    val estado_conexion: Boolean,
    val calidad_conexion: Int
)

@Entity(
    tableName = "dispositivos",
    foreignKeys = [
        ForeignKey(
            entity = GrupoEntity::class,
            parentColumns = ["id_grupo"],
            childColumns = ["id_grupo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GatewayEntity::class,
            parentColumns = ["id_gateway"],
            childColumns = ["id_gateway"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class DispositivoEntity(
    @PrimaryKey val id_dispositivo: String,
    val nombre: String,
    val tipo: String,
    val estado: Boolean,
    val consumo_actual: Double,
    val id_gateway: String?,
    val id_grupo: String
)

@Entity(
    tableName = "configuracion_consumo",
    foreignKeys = [
        ForeignKey(
            entity = DispositivoEntity::class,
            parentColumns = ["id_dispositivo"],
            childColumns = ["id_dispositivo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ConfiguracionConsumoEntity(
    @PrimaryKey val id_config: String,
    val limite_kwh: Double,
    val tiempo_maximo: Long,
    val apagado_auto: Boolean,
    val id_dispositivo: String
)

@Entity(
    tableName = "consumo",
    foreignKeys = [
        ForeignKey(
            entity = DispositivoEntity::class,
            parentColumns = ["id_dispositivo"],
            childColumns = ["id_dispositivo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ConsumoEntity(
    @PrimaryKey val id_consumo: String,
    val kwh: Double,
    val costo: Double,
    val fecha: Long,
    val id_dispositivo: String
)

@Entity(
    tableName = "alertas",
    foreignKeys = [
        ForeignKey(
            entity = DispositivoEntity::class,
            parentColumns = ["id_dispositivo"],
            childColumns = ["id_dispositivo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AlertaEntity(
    @PrimaryKey val id_alerta: String,
    val tipo_alerta: String,
    val fecha: Long,
    val estado: String,
    val id_dispositivo: String
)
