package com.example.appsigee.ui.screens.dispositivos// ui/screens/dispositivos/DispositivosScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField

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

    // Estados para el diálogo de edición de grupo
    var showEditDialog by remember { mutableStateOf(false) }
    var groupToEditId by remember { mutableStateOf("") }
    var groupToEditName by remember { mutableStateOf("") }
    var newGroupName by remember { mutableStateOf("") }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar nombre del grupo") },
            text = {
                Column {
                    Text("Nombre actual: $groupToEditName")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = newGroupName,
                        onValueChange = { newGroupName = it },
                        label = { Text("Nuevo nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newGroupName.isNotBlank()) {
                            viewModel.updateGrupoNombre(groupToEditId, newGroupName)
                            showEditDialog = false
                        }
                    }
                ) {
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
            val items = listOf("Saldo", "Dispositivos", "Recibos", "Reportes", "Ubícanos")
            val icons = listOf(
                Icons.Outlined.Home,
                Icons.Outlined.Devices,
                Icons.Outlined.ReceiptLong,
                Icons.Outlined.ElectricBolt,
                Icons.Outlined.LocationOn
            )

            var selectedItem by remember { mutableStateOf(1) } // 1 es "Dispositivos" según tu imagen

            NavigationBar(
                containerColor = Color.White, // Fondo blanco como en la imagen
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = icons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(text = item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50), // Verde de la imagen
                            selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent // Elimina el círculo de fondo al seleccionar
                        )
                    )
                }
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
                        groupToEditId = id
                        groupToEditName = nombre
                        newGroupName = nombre
                        showEditDialog = true
                    }
                )
            }
        }
    }
}