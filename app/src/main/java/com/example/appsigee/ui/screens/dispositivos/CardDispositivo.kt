package com.example.appsigee.ui.screens.dispositivos// ui/screens/dispositivos/components/CardDispositivo.kt

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importa R de tu proyecto
import com.example.appsigee.R
// Importa tu modelo Dispositivo
import com.example.appsigee.domain.model.Dispositivo
import com.example.appsigee.domain.model.TipoDispositivo

@Composable
fun CardDispositivo(
    dispositivo: Dispositivo,
    onClick: () -> Unit = {}
) {
    // Determinar color de borde
    val colorBorde = if (dispositivo.estaEnAlerta) Color.Red else Color(0xFF66BB6A) // Un verde suave

    // El contenedor principal del Card
    Card(
        modifier = Modifier
            .width(110.dp) // Ancho aproximado de la imagen
            .padding(4.dp),
        border = BorderStroke(1.dp, colorBorde),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // IMAGEN O ICONO
            if (dispositivo.esBotonNuevo) {
                // Botón de "+"
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Nuevo",
                    modifier = Modifier.size(70.dp).padding(8.dp),
                    tint = Color.Black
                )
            } else {
                // Mapeo de TipoDispositivo a Icono de Material
                Icon(
                    imageVector = getIconForTipo(dispositivo.tipo),
                    contentDescription = dispositivo.nombre,
                    modifier = Modifier.size(70.dp).padding(8.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // NOMBRE
            Text(
                text = dispositivo.nombre,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1
            )

            // COSTO
            Text(
                text = "$${dispositivo.costoSemanas}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            // CONSUMO
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Pequeño icono de rayo (Necesitarás el vector)
                Text(text = "⚡", color = Color(0xFFFFC107), fontSize = 12.sp) // Emoji por ahora
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${dispositivo.consumoKwh} kWh",
                    color = Color(0xFF2E7D32), // Verde oscuro
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

fun getIconForTipo(tipo: TipoDispositivo): ImageVector {
    return when (tipo) {
        TipoDispositivo.TELEVISION -> Icons.Default.Tv
        TipoDispositivo.CONSOLA -> Icons.Default.VideogameAsset
        TipoDispositivo.FOCO -> Icons.Default.Lightbulb
        TipoDispositivo.REFRIGERADOR -> Icons.Default.Kitchen
        TipoDispositivo.MICROONDAS -> Icons.Default.Microwave
        TipoDispositivo.LAPTOP -> Icons.Default.Laptop
        TipoDispositivo.CELULAR -> Icons.Default.Smartphone
        else -> Icons.Default.Devices
    }
}