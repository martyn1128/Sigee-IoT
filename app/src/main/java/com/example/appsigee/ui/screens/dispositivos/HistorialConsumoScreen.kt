package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appsigee.domain.model.TipoDispositivo
import com.example.appsigee.ui.screens.components.BottomNavBar
import com.example.appsigee.ui.utils.getIconForTipo
import com.example.appsigee.ui.viewmodel.DispositivosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialConsumoScreen(
    idDispositivo: String,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: DispositivosViewModel
) {
    val dispositivoFlow = remember(idDispositivo) { viewModel.getDispositivoById(idDispositivo) }
    val dispositivo by dispositivoFlow.collectAsState(initial = null)
    val habitaciones by viewModel.habitaciones.collectAsState()
    
    val habitacionNombre = remember(habitaciones, dispositivo) {
        habitaciones.find { h -> h.dispositivos.any { d -> d.id == idDispositivo } }?.nombre ?: ""
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "HISTORIAL DE CONSUMO",
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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            // Título: Laptop - Habitación Juan
            Text(
                text = "${dispositivo?.nombre ?: ""} - $habitacionNombre",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen del dispositivo
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val tipoStr = dispositivo?.tipo ?: ""
                if (tipoStr.startsWith("content://") || tipoStr.startsWith("file://")) {
                    AsyncImage(
                        model = tipoStr,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = getIconForTipo(tipoStr),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFF4CAF50).copy(alpha = 0.5f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Periodo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Periodo Diciembre-Febrero",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card del Gráfico
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Gráfico consumo energético - semanas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.size(18.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Representación del gráfico
                    ConsumptionGraph(modifier = Modifier.height(150.dp).fillMaxWidth())

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("4-10 enero", fontSize = 10.sp, color = Color.Gray)
                        Text("11-17", fontSize = 10.sp, color = Color.Gray)
                        Text("18-24", fontSize = 10.sp, color = Color.Gray)
                        Text("25-31", fontSize = 10.sp, color = Color.Gray)
                        Text("1-7 feb", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sección Resumen
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Resumen",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = { /* Descargar */ },
                    modifier = Modifier.size(32.dp).background(Color(0xFF42A5F5), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Descargar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Consumo energético: 364 kWh",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Costo en pesos: $189.41",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ConsumptionGraph(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        // Ejes y líneas de fondo
        val steps = 5
        for (i in 0..steps) {
            val y = height - (height / steps) * i
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        // Datos simulados para el gráfico de área
        val points = listOf(
            0.1f, 0.15f, 0.3f, 0.25f, 0.4f, 0.5f, 0.45f, 0.6f, 0.55f, 0.75f, 0.7f, 0.9f
        )
        
        val path = Path()
        val areaPath = Path()
        
        points.forEachIndexed { index, value ->
            val x = (width / (points.size - 1)) * index
            val y = height - (height * value)
            
            if (index == 0) {
                path.moveTo(x, y)
                areaPath.moveTo(x, height)
                areaPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                areaPath.lineTo(x, y)
            }
            
            if (index == points.size - 1) {
                areaPath.lineTo(x, height)
                areaPath.close()
            }
        }

        // Dibujar el degradado del área
        drawPath(
            path = areaPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Red.copy(alpha = 0.6f),
                    Color.Yellow.copy(alpha = 0.6f),
                    Color.Green.copy(alpha = 0.6f)
                ),
                startY = 0f,
                endY = height
            )
        )

        // Dibujar la línea
        drawPath(
            path = path,
            color = Color(0xFF42A5F5),
            style = Stroke(width = 2.dp.toPx())
        )
        
        // Punto final
        val lastX = width
        val lastY = height - (height * points.last())
        drawCircle(
            color = Color(0xFF42A5F5),
            radius = 4.dp.toPx(),
            center = androidx.compose.ui.geometry.Offset(lastX, lastY)
        )
    }
}
