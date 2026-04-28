package com.example.appsigee.ui.screens.dispositivos// ui/screens/dispositivos/DispositivosScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DispositivosScreen(onNuevoClick: () -> Unit = {}) {
    val context = LocalContext.current
    val app = context.applicationContext as SigeeApplication
    val repository = DispositivoRepository(app.database.dispositivoDao())
    val factory = DispositivosViewModelFactory(repository, app.database.grupoDao())
    val viewModel: DispositivosViewModel = viewModel(factory = factory)

    // Obtenemos el estado del ViewModel
    val habitaciones by viewModel.habitaciones.collectAsState()

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
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Saldo") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Config") },
                    label = { Text("Dispositivos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Config") },
                    label = { Text("Recibos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Config") },
                    label = { Text("Reportes") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Config") },
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
                    onNuevoClick = onNuevoClick
                )
            }
        }
    }
}