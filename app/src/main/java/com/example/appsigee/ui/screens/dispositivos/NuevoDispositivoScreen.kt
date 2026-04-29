package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsigee.data.local.entity.GrupoEntity
import com.example.appsigee.domain.model.TipoDispositivo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoDispositivoScreen(
    idExistente: String,
    grupoIdInicial: String,
    listaGrupos: List<GrupoEntity>,
    onBack: () -> Unit,
    onSaveClick: (String, String, String) -> Unit // nombre, grupoId, tipo
) {
    var nombre by remember { mutableStateOf("") }
    
    // Buscar el grupo inicial por ID para mostrar su nombre
    val grupoInicial = listaGrupos.find { it.id_grupo == grupoIdInicial }
    var grupoSeleccionado by remember { mutableStateOf(grupoInicial) }
    var expandedGrupo by remember { mutableStateOf(false) }

    var tipoSeleccionado by remember { mutableStateOf(TipoDispositivo.TELEVISION) }
    var showImagePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "NUEVO DISPOSITIVO",
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
                "Registra un nuevo dispositivo:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo ID - Solo lectura
            FormField(label = "ID", value = idExistente, onValueChange = {}, placeholder = "", readOnly = true)

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

            // BOTÓN GUARDAR
            Button(
                onClick = {
                    if (nombre.isNotBlank() && grupoSeleccionado != null) {
                        onSaveClick(nombre, grupoSeleccionado!!.id_grupo, tipoSeleccionado.name)
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008542)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Guardar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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

@Composable
fun SeccionImagenSeleccionable(tipo: TipoDispositivo, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text("Imagen:", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.clickable { onClick() }
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Gray),
                color = Color.Transparent
            ) {
                Icon(
                    imageVector = getIconForTipo(tipo),
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp).size(80.dp)
                )
            }
            Icon(
                Icons.Default.Edit,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .offset(x = 8.dp, y = (-8).dp)
                    .size(28.dp)
                    .background(Color.White, shape = RoundedCornerShape(14.dp))
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun ImagePickerDialog(onDismiss: () -> Unit, onTipoSelected: (TipoDispositivo) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona una imagen") },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(300.dp)
            ) {
                items(TipoDispositivo.entries.filter { it != TipoDispositivo.NUEVO }) { tipo ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onTipoSelected(tipo) }
                    ) {
                        Icon(
                            imageVector = getIconForTipo(tipo),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(tipo.name, fontSize = 10.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        TextField(
            value = value,
            onValueChange = { if (!readOnly) onValueChange(it) },
            modifier = Modifier.weight(1f),
            readOnly = readOnly,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F1F1),
                unfocusedContainerColor = Color(0xFFF1F1F1),
                disabledContainerColor = Color(0xFFEBEBEB),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )
    }
}
