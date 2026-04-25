package com.example.appsigee.ui.screens.dispositivos// ui/screens/dispositivos/DispositivosScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
// Importa FilaHabitacion y DispositivosViewModel
import com.example.appsigee.ui.screens.components.FilaHabitacion
import com.example.appsigee.ui.viewmodel.DispositivosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DispositivosScreen(viewModel: DispositivosViewModel = viewModel()) {

    // Obtenemos el estado del ViewModel
    val habitaciones by viewModel.habitaciones.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("DISPOSITIVOS DEL HOGAR", color = Color(0xFF2E7D32)) // Verde oscuro
                }
            )
        },
        bottomBar = {
            // Aquí iría tu barra de navegación (Paso 4)
        }
    ) { innerPadding ->

        // LazyColumn es como un RecyclerView vertical
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(habitaciones) { habitacion ->
                FilaHabitacion(seccion = habitacion)
            }
        }
    }
}