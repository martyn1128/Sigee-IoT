package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Microwave
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsigee.domain.model.TipoDispositivo
import com.example.appsigee.ui.screens.components.BottomNavBar
import com.example.appsigee.ui.viewmodel.DispositivosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionConsumoScreen(
    idDispositivo: String,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: DispositivosViewModel
) {
    val dispositivoFlow = remember(idDispositivo) { viewModel.getDispositivoById(idDispositivo) }
    val dispositivo by dispositivoFlow.collectAsState(initial = null)
    
    val configuracionFlow = remember(idDispositivo) { viewModel.getConfiguracion(idDispositivo) }
    val configuracion by configuracionFlow.collectAsState(initial = null)
    
    val habitaciones by viewModel.habitaciones.collectAsState()
    
    val habitacionNombre = remember(habitaciones, dispositivo) {
        habitaciones.find { h -> h.dispositivos.any { d -> d.id == idDispositivo } }?.nombre ?: ""
    }
    
    var limiteKwh by remember { mutableStateOf("") }
    var tiempoMaximo by remember { mutableStateOf("") }
    var apagarAuto by remember { mutableStateOf(false) }
    
    var isInitialized by remember { mutableStateOf(false) }
    
    val focusManager = LocalFocusManager.current

    // Sincronizar estados locales con los datos de la BD al cargar (SOLO LA PRIMERA VEZ)
    LaunchedEffect(configuracion) {
        if (configuracion != null && !isInitialized) {
            limiteKwh = configuracion!!.limite_kwh.toString()
            tiempoMaximo = configuracion!!.tiempo_maximo.toString()
            apagarAuto = configuracion!!.apagado_auto
            isInitialized = true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("CONF. CONSUMO MÁXIMO", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (configuracion != null) {
                        IconButton(onClick = { 
                            viewModel.deleteConfiguracion(idDispositivo)
                            onBack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Imagen del dispositivo
            Icon(
                imageVector = getIconForTipo(dispositivo?.tipo ?: TipoDispositivo.MICROONDAS),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.LightGray
            )
            
            HorizontalDivider(color = Color(0xFF4CAF50), thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
            
            // Límite de consumo
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Límite de consumo permitido", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = limiteKwh,
                        onValueChange = { limiteKwh = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(28.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        singleLine = true,
                        placeholder = { Text("0.0") }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("kWh", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Tiempo máximo
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Tiempo máximo de uso", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = tiempoMaximo,
                        onValueChange = { tiempoMaximo = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(28.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        singleLine = true,
                        placeholder = { Text("0") }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Horas", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                }
            }
            
            HorizontalDivider(color = Color(0xFF4CAF50), thickness = 1.dp, modifier = Modifier.padding(vertical = 24.dp))
            
            // Apagar automáticamente
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Apagar automáticamente", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Checkbox(
                    checked = apagarAuto,
                    onCheckedChange = { apagarAuto = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Botón Guardar
            Button(
                onClick = { 
                    val limite = limiteKwh.toDoubleOrNull() ?: 0.0
                    val tiempo = tiempoMaximo.toLongOrNull() ?: 0L
                    viewModel.saveConfiguracion(idDispositivo, limite, tiempo, apagarAuto)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008941)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Guardar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
