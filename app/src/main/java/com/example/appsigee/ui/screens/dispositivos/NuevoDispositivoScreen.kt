package com.example.appsigee.ui.screens.dispositivos

import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.appsigee.data.local.entity.GrupoEntity
import com.example.appsigee.domain.model.TipoDispositivo
import com.example.appsigee.ui.screens.components.BottomNavBar
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoDispositivoScreen(
    idExistente: String,
    grupoIdInicial: String,
    listaGrupos: List<GrupoEntity>,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    onSaveClick: (String, String, String) -> Unit // nombre, grupoId, tipo
) {
    var nombre by remember { mutableStateOf("") }
    
    val grupoInicial = listaGrupos.find { it.id_grupo == grupoIdInicial }
    var grupoSeleccionado by remember { mutableStateOf(grupoInicial) }
    var expandedGrupo by remember { mutableStateOf(false) }

    var tipoSeleccionado by remember { mutableStateOf(TipoDispositivo.TELEVISION.name) }
    var showImagePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { tipoSeleccionado = it.toString() }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            photoUri?.let { tipoSeleccionado = it.toString() }
        }
    }

    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

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
                "Registra un nuevo dispositivo:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            FormField(label = "ID", value = idExistente, onValueChange = {}, placeholder = "", readOnly = true)

            Spacer(modifier = Modifier.height(16.dp))

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

            FormField(
                label = "Nombre:",
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = "Ej. Lámpara Sala"
            )

            Spacer(modifier = Modifier.height(24.dp))

            SeccionImagenSeleccionable(
                tipo = tipoSeleccionado,
                onClick = { showImagePicker = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && grupoSeleccionado != null) {
                        onSaveClick(nombre, grupoSeleccionado!!.id_grupo, tipoSeleccionado)
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
                },
                onGalleryClick = {
                    galleryLauncher.launch("image/*")
                    showImagePicker = false
                },
                onCameraClick = {
                    val photoFile = createImageFile()
                    photoUri = FileProvider.getUriForFile(
                        context,
                        "com.example.appsigee.fileprovider",
                        photoFile
                    )
                    cameraLauncher.launch(photoUri!!)
                    showImagePicker = false
                }
            )
        }
    }
}
