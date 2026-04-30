package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsigee.domain.model.AlertaConsumo
import com.example.appsigee.ui.screens.components.BottomNavBar
import com.example.appsigee.ui.viewmodel.DispositivosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertasScreen(
    idDispositivo: String,
    onBack: () -> Unit,
    onConfigClick: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: DispositivosViewModel
) {
    val dispositivo by viewModel.getDispositivoById(idDispositivo).collectAsState(initial = null)
    
    // Datos de ejemplo para la lista de alertas
    val alertas = remember {
        listOf(
            AlertaConsumo("1", "Límite de tiempo", "18/03/2026 - 4:03 am", "ALERTA"),
            AlertaConsumo("2", "Límite de tiempo", "02/03/2026 - 11:17 pm", "OK"),
            AlertaConsumo("3", "Límite de tiempo", "11/02/2026 - 06:23 pm", "OK"),
            AlertaConsumo("4", "Exceso de consumo", "10/02/2026 - 01:17 pm", "OK"),
            AlertaConsumo("5", "Exceso de consumo", "15/01/2026 - 08:34 am", "OK")
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("ALERTAS DE CONSUMO", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(currentRoute = "dispositivos", onNavigate = onNavigate)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${dispositivo?.nombre ?: "Foco1"} - Sala",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Imagen del dispositivo (Placeholder: Foco)
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.LightGray
            )
            
            HorizontalDivider(color = Color(0xFF4CAF50), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Listas de alertas generadas", fontWeight = FontWeight.Medium)
                }
                Icon(Icons.Default.ArrowDropUp, contentDescription = null, tint = Color(0xFF2E7D32))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Cabecera de la tabla
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tipo de alerta", color = Color(0xFF4CAF50), fontSize = 14.sp, modifier = Modifier.weight(1.2f))
                Text("Fecha y hora", color = Color(0xFF4CAF50), fontSize = 14.sp, modifier = Modifier.weight(1.5f))
                Text("Estado", color = Color(0xFF4CAF50), fontSize = 14.sp, modifier = Modifier.weight(0.5f))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(alertas) { alerta ->
                    AlertaRow(alerta)
                }
            }
            
            HorizontalDivider(color = Color(0xFF4CAF50), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
            
            // Link a configuración
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onConfigClick() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Configuración de consumo máximo", modifier = Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF4CAF50))
            }
        }
    }
}

@Composable
fun AlertaRow(alerta: AlertaConsumo) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(alerta.tipo, fontSize = 13.sp, modifier = Modifier.weight(1.2f))
            Text(alerta.fechaHora, fontSize = 13.sp, modifier = Modifier.weight(1.5f))
            Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
                if (alerta.estado == "ALERTA") {
                    Icon(Icons.Default.Warning, contentDescription = "Alerta", modifier = Modifier.size(16.dp))
                } else {
                    Icon(Icons.Default.Check, contentDescription = "OK", modifier = Modifier.size(16.dp))
                }
            }
        }
        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
    }
}
