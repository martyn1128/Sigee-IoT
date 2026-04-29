package com.example.appsigee.ui.screens.components// ui/screens/dispositivos/components/FilaHabitacion.kt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importa SeccionHabitacion
import com.example.appsigee.domain.model.SeccionHabitacion
// Importa CardDispositivo
import com.example.appsigee.ui.screens.dispositivos.CardDispositivo
@Composable
fun FilaHabitacion(
    seccion: SeccionHabitacion,
    onNuevoClick: (String, String, String) -> Unit = { _, _, _ -> },
    onDispositivoClick: (String) -> Unit = {},
    onEditGrupoClick: (String, String) -> Unit = { _, _ -> }
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {

        // CABECERA (Nombre sala + editar + flecha)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onEditGrupoClick(seccion.id_grupo, seccion.nombre) }
            ) {
                Text(text = seccion.nombre, fontSize = 20.sp, color = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                // Icono editar (Necesitas el vector)
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
            }

            // Botón/Flecha de "ver más"
            IconButton(onClick = { /* Ir a detalle sala */ }) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Ver más",
                    tint = Color(0xFF2E7D32)
                )
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))

        // LISTA HORIZONTAL
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(seccion.dispositivos) { dispositivo ->
                CardDispositivo(
                    dispositivo = dispositivo,
                    onClick = {
                        if (dispositivo.esBotonNuevo) {
                            onNuevoClick(dispositivo.id, seccion.nombre, seccion.id_grupo)
                        } else {
                            onDispositivoClick(dispositivo.id)
                        }
                    }
                )
            }
        }
    }
}