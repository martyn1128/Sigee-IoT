package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appsigee.domain.model.Dispositivo
import com.example.appsigee.domain.model.TipoDispositivo
import com.example.appsigee.ui.screens.components.BottomNavBar
import com.example.appsigee.ui.utils.getIconForTipo
import com.example.appsigee.ui.viewmodel.DispositivosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlDispositivoScreen(
    idDispositivo: String,
    onBack: () -> Unit,
    onAlertasClick: () -> Unit,
    onConfigClick: () -> Unit,
    onHistorialClick: () -> Unit,
    onEditClick: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: DispositivosViewModel
) {
    // Cargamos el dispositivo desde el ViewModel
    val dispositivoFlow = remember(idDispositivo) { viewModel.getDispositivoById(idDispositivo) }
    val dispositivo by dispositivoFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "CONTROL DE DISPOSITIVO",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre y Editar
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = dispositivo?.nombre ?: "Cargando...",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onEditClick() },
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen del dispositivo
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                val tipo = dispositivo?.tipo ?: ""
                if (tipo.startsWith("content://") || tipo.startsWith("file://")) {
                    AsyncImage(
                        model = tipo,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = getIconForTipo(tipo),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Métricas
            MetricRow(
                icon = Icons.Outlined.ElectricBolt,
                label = "Consumo energético actual:",
                value = "${dispositivo?.consumoKwh ?: 0} kWh",
                valueColor = Color(0xFF4CAF50),
                showArrow = true,
                onClick = onHistorialClick
            )

            MetricRow(
                icon = Icons.Outlined.Payments,
                label = "Costo estimado en pesos:",
                value = "$${dispositivo?.costoSemanas ?: 0.0}",
                valueColor = Color(0xFF4CAF50)
            )

            HorizontalDivider(color = Color(0xFF4CAF50), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

            // Estado
            Row(verticalAlignment = Alignment.CenterVertically) {
                /*
                Icon(
                    imageVector = Icons.Default.CheckBox,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
                 */
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Estado del dispositivo: ${if (dispositivo?.estado == true) "Activo" else "Inactivo"}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Apagar
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = CircleShape,
                    color = if (dispositivo?.estado == true) Color(0xFFE57373) else Color.Gray,
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { 
                            dispositivo?.let { 
                                viewModel.toggleDispositivoEstado(it)
                            }
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = if (dispositivo?.estado == true) "Apagar" else "Encender",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Text(text = if (dispositivo?.estado == true) "Apagar" else "Encender", modifier = Modifier.padding(top = 4.dp))
            }

            HorizontalDivider(color = Color(0xFF4CAF50), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

            // Menú de opciones
            MenuItem(icon = Icons.Default.Notifications, label = "Alertas de consumo", onClick = onAlertasClick)
            MenuItem(icon = Icons.Default.Settings, label = "Configuración de consumo máximo", onClick = onConfigClick)
            MenuItem(icon = Icons.Outlined.History, label = "Historial de consumo", onClick = onHistorialClick)
        }
    }
}

@Composable
fun MetricRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color,
    showArrow: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = showArrow) { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, modifier = Modifier.weight(1f), fontSize = 14.sp)
        Text(text = value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun MenuItem(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = label, modifier = Modifier.weight(1f), fontSize = 16.sp)
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(24.dp)
            )
        }
        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
    }
}
