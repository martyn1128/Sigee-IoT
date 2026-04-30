package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsigee.data.local.entity.GrupoEntity
import com.example.appsigee.domain.model.TipoDispositivo
import com.example.appsigee.ui.screens.components.BottomNavBar
import com.example.appsigee.ui.viewmodel.DispositivosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarDispositivoScreen(
    idDispositivo: String,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: DispositivosViewModel
) {
    val dispositivoFlow = remember(idDispositivo) { viewModel.getDispositivoById(idDispositivo) }
    val dispositivo by dispositivoFlow.collectAsState(initial = null)
    val listaGrupos by viewModel.grupos.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var grupoSeleccionado by remember { mutableStateOf<GrupoEntity?>(null) }
    var expandedGrupo by remember { mutableStateOf(false) }
    var tipoSeleccionado by remember { mutableStateOf(TipoDispositivo.TELEVISION) }
    var showImagePicker by remember { mutableStateOf(false) }

    var isInitialized by remember { mutableStateOf(false) }

    // Sincronizar con datos actuales
    LaunchedEffect(dispositivo, listaGrupos) {
        if (dispositivo != null && listaGrupos.isNotEmpty() && !isInitialized) {
            nombre = dispositivo!!.nombre
            tipoSeleccionado = dispositivo!!.tipo
            
            // Buscar el grupo actual en la lista cargada
            val habitacion = viewModel.habitaciones.value.find { h -> 
                h.dispositivos.any { d -> d.id == idDispositivo } 
            }
            grupoSeleccionado = listaGrupos.find { it.id_grupo == habitacion?.id_grupo }
            isInitialized = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "ACTUALIZAR DISPOSITIVO",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Atrás")
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Actualiza los datos del dispositivo:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo ID - Solo lectura
            FormField(label = "ID", value = idDispositivo, onValueChange = {}, placeholder = "", readOnly = true)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Grupo - Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                ExposedDropdownMenuBox(
                    expanded = expandedGrupo,
                    onExpandedChange = { expandedGrupo = !expandedGrupo }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Grupo:", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        TextField(
                            value = grupoSeleccionado?.nombre ?: "Seleccionar Grupo",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.weight(1f),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGrupo) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF1F1F1),
                                unfocusedContainerColor = Color(0xFFF1F1F1),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }

                    ExposedDropdownMenu(
                        expanded = expandedGrupo,
                        onDismissRequest = { expandedGrupo = false }
                    ) {
                        listaGrupos.forEach { grupo ->
                            DropdownMenuItem(
                                text = { Text(grupo.nombre) },
                                onClick = {
                                    grupoSeleccionado = grupo
                                    expandedGrupo = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nombre - Editable
            FormField(
                label = "Nombre:",
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = "Ej. Lámpara Sala"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de Imagen
            SeccionImagenSeleccionable(
                tipo = tipoSeleccionado,
                onClick = { showImagePicker = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            // BOTÓN ACTUALIZAR
            Button(
                onClick = {
                    if (nombre.isNotBlank() && grupoSeleccionado != null) {
                        viewModel.updateDispositivo(
                            id = idDispositivo,
                            nombre = nombre,
                            grupoId = grupoSeleccionado!!.id_grupo,
                            tipo = tipoSeleccionado.name
                        )
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008542)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Actualizar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        if (showImagePicker) {
            ImagePickerDialog(
                onDismiss = { showImagePicker = false },
                onTipoSelected = {
                    tipoSeleccionado = it
                    showImagePicker = false
                }
            )
        }
    }
}
