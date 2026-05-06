package com.example.appsigee.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.appsigee.domain.model.TipoDispositivo

fun getIconForTipo(tipo: String): ImageVector {
    return when (tipo) {
        TipoDispositivo.TELEVISION.name -> Icons.Default.Tv
        TipoDispositivo.CONSOLA.name -> Icons.Default.VideogameAsset
        TipoDispositivo.FOCO.name -> Icons.Default.Lightbulb
        TipoDispositivo.REFRIGERADOR.name -> Icons.Default.Kitchen
        TipoDispositivo.MICROONDAS.name -> Icons.Default.Microwave
        TipoDispositivo.LAPTOP.name -> Icons.Default.Laptop
        TipoDispositivo.CELULAR.name -> Icons.Default.Smartphone
        else -> Icons.Default.Devices
    }
}
