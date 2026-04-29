package com.example.appsigee.ui.screens.dispositivos// ui/screens/dispositivos/DispositivosScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
// Importa FilaHabitacion y DispositivosViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.appsigee.SigeeApplication
import com.example.appsigee.data.repository.DispositivoRepository
import com.example.appsigee.ui.screens.components.FilaHabitacion
import com.example.appsigee.ui.viewmodel.DispositivosViewModel
import com.example.appsigee.ui.viewmodel.DispositivosViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DispositivosScreen(onNuevoClick: (String, String, String) -> Unit = { _, _, _ -> }) {
    val context = LocalContext.current
    val app = context.applicationContext as SigeeApplication
    val repository = DispositivoRepository(app.database.dispositivoDao())
    val factory = DispositivosViewModelFactory(repository, app.database.grupoDao())
    val viewModel: DispositivosViewModel = viewModel(factory = factory)

    // Obtenemos el estado del ViewModel
    val habitaciones by viewModel.habitaciones.collectAsState()

    // Estado para el diálogo de edición de grupo
    var showEditDialog by remember { mutableStateOf(false) }
    var grupoAEditarId by remember { mutableStateOf("") }
    var nuevoNombreGrupo by remember { mutableStateOf("") }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar nombre del grupo") },
            text = {
                TextField(
                    value = nuevoNombreGrupo,
                    onValueChange = { nuevoNombreGrupo = it },
                    label = { Text("Nuevo nombre") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (nuevoNombreGrupo.isNotBlank()) {
                        viewModel.updateGrupoNombre(grupoAEditarId, nuevoNombreGrupo)
                        showEditDialog = false
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("DISPOSITIVOS DEL HOGAR", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold) // Verde oscuro
                }
            )
        },
        bottomBar = {
            // Aquí iría tu barra de navegación (Paso 4)
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Inicio") },
                    label = { Text("Saldo") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Devices, contentDescription = "Config") },
                    label = { Text("Dispositivos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Receipt, contentDescription = "Recibos") },
                    label = { Text("Recibos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Reportes") },
                    label = { Text("Reportes") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Place, contentDescription = "Ubicanos") },
                    label = { Text("Ubicanos") }
                )
            }
        }
    ) { innerPadding ->

        // LazyColumn es como un RecyclerView vertical
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(habitaciones) { habitacion ->
                FilaHabitacion(
                    seccion = habitacion,
                    onNuevoClick = onNuevoClick,
                    onEditGrupoClick = { id, nombre ->
                        grupoAEditarId = id
                        nuevoNombreGrupo = nombre
                        showEditDialog = true
                    }
                )
            }
        }
    }
}
