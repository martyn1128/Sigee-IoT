package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsigee.data.local.entity.AlertaEntity
import coil.compose.AsyncImage
import com.example.appsigee.domain.model.TipoDispositivo
import com.example.appsigee.ui.screens.components.BottomNavBar
import com.example.appsigee.ui.utils.getIconForTipo
import com.example.appsigee.ui.viewmodel.DispositivosViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertasScreen(
    idDispositivo: String,
    onBack: () -> Unit,
    onConfigClick: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: DispositivosViewModel
) {
    val dispositivoFlow = remember(idDispositivo) { viewModel.getDispositivoById(idDispositivo) }
    val dispositivo by dispositivoFlow.collectAsState(initial = null)
    
    val alertasFlow = remember(idDispositivo) { viewModel.getAlertas(idDispositivo) }
    val alertas by alertasFlow.collectAsState(initial = emptyList())
    
    val habitaciones by viewModel.habitaciones.collectAsState()
    val habitacionNombre = remember(habitaciones, dispositivo) {
        habitaciones.find { h -> h.dispositivos.any { d -> d.id == idDispositivo } }?.nombre ?: ""
    }

    var isExpanded by remember { mutableStateOf(false) }

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
                text = "${dispositivo?.nombre ?: ""} - $habitacionNombre",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Imagen del dispositivo
            val tipoStr = dispositivo?.tipo ?: ""
            if (tipoStr.startsWith("content://") || tipoStr.startsWith("file://")) {
                AsyncImage(
                    model = tipoStr,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            } else {
                Icon(
                    imageVector = getIconForTipo(tipoStr),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = Color.LightGray
                )
            }
            
            HorizontalDivider(color = Color(0xFF4CAF50), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
            
            // Lista desplegable de alertas
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isExpanded = !isExpanded }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Listas de alertas generadas", fontWeight = FontWeight.Medium)
                        }
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32)
                        )
                    }

                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                            if (alertas.isEmpty()) {
                                Text("No hay alertas registradas", modifier = Modifier.padding(16.dp), color = Color.Gray)
                            } else {
                                // Cabecera de la tabla
                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Tipo de alerta", color = Color(0xFF4CAF50), fontSize = 14.sp, modifier = Modifier.weight(1.2f))
                                    Text("Fecha y hora", color = Color(0xFF4CAF50), fontSize = 14.sp, modifier = Modifier.weight(1.5f))
                                    Text("Estado", color = Color(0xFF4CAF50), fontSize = 14.sp, modifier = Modifier.weight(0.5f))
                                }
                                
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(alertas) { alerta ->
                                        AlertaRow(alerta)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            HorizontalDivider(color = Color(0xFF4CAF50), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
            
            // Link a configuración (FIJO ABAJO)
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
fun AlertaRow(alerta: AlertaEntity) {
    val sdf = SimpleDateFormat("dd/MM/yyyy - hh:mm a", Locale.getDefault())
    val fechaStr = sdf.format(Date(alerta.fecha))

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(alerta.tipo_alerta, fontSize = 13.sp, modifier = Modifier.weight(1.2f))
            Text(fechaStr, fontSize = 13.sp, modifier = Modifier.weight(1.5f))
            Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
                if (alerta.estado == "ALERTA") {
                    Icon(Icons.Default.Warning, contentDescription = "Alerta", modifier = Modifier.size(16.dp), tint = Color.Red)
                } else {
                    Icon(Icons.Default.Check, contentDescription = "OK", modifier = Modifier.size(16.dp), tint = Color(0xFF4CAF50))
                }
            }
        }
        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
    }
}
