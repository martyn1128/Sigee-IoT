package com.example.appsigee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appsigee.data.local.entity.DispositivoEntity
import com.example.appsigee.data.repository.DispositivoRepository
import com.example.appsigee.ui.screens.dispositivos.ActualizarDispositivoScreen
import com.example.appsigee.ui.screens.dispositivos.AlertasScreen
import com.example.appsigee.ui.screens.dispositivos.ConfiguracionConsumoScreen
import com.example.appsigee.ui.screens.dispositivos.ControlDispositivoScreen
import com.example.appsigee.ui.screens.dispositivos.HistorialConsumoScreen
import com.example.appsigee.ui.screens.dispositivos.DispositivosScreen
import com.example.appsigee.ui.screens.dispositivos.NuevoDispositivoScreen
import com.example.appsigee.ui.screens.recibos.RecibosScreen
import com.example.appsigee.ui.screens.reportes.ReportesScreen
import com.example.appsigee.ui.screens.saldo.SaldoScreen
import com.example.appsigee.ui.screens.ubicanos.UbicanosScreen
import com.example.appsigee.ui.theme.AppSigeeTheme
import com.example.appsigee.ui.viewmodel.DispositivosViewModel
import com.example.appsigee.ui.viewmodel.DispositivosViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContent {
            AppSigeeTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val app = context.applicationContext as SigeeApplication
                val scope = rememberCoroutineScope()

                // Necesitamos el repositorio para hacer el UPDATE
                val repository = DispositivoRepository(app.database.dispositivoDao())
                val factory = DispositivosViewModelFactory(
                    repository,
                    app.database.grupoDao(),
                    app.database.configuracionConsumoDao(),
                    app.database.alertaDao()
                )
                val viewModel: DispositivosViewModel = viewModel(factory = factory)
                val listaGrupos by viewModel.grupos.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "saldo"
                ) {
                    // Pantalla de Saldo (Principal)
                    composable("saldo") {
                        SaldoScreen(onNavigate = { route -> navController.navigate(route) })
                    }

                    // Pantalla Principal de Dispositivos
                    composable("dispositivos") {
                        DispositivosScreen(
                            onNuevoClick = { id, grupoNombre, grupoId ->
                                navController.navigate("nuevo_dispositivo/$id/$grupoNombre/$grupoId")
                            },
                            onDispositivoClick = { id ->
                                navController.navigate("control_dispositivo/$id")
                            },
                            onNavigate = { route -> navController.navigate(route) }
                        )
                    }

                    // Pantalla de Recibos
                    composable("recibos") {
                        RecibosScreen(onNavigate = { route -> navController.navigate(route) })
                    }

                    // Pantalla de Reportes
                    composable("reportes") {
                        ReportesScreen(onNavigate = { route -> navController.navigate(route) })
                    }

                    // Pantalla de Ubícanos
                    composable("ubicanos") {
                        UbicanosScreen(onNavigate = { route -> navController.navigate(route) })
                    }

                    // Pantalla de Control de Dispositivo
                    composable(
                        route = "control_dispositivo/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        ControlDispositivoScreen(
                            idDispositivo = id,
                            onBack = { navController.popBackStack() },
                            onAlertasClick = { navController.navigate("alertas_dispositivo/$id") },
                            onConfigClick = { navController.navigate("config_consumo/$id") },
                            onHistorialClick = { navController.navigate("historial_consumo/$id") },
                            onEditClick = { navController.navigate("actualizar_dispositivo/$id") },
                            onNavigate = { route -> navController.navigate(route) },
                            viewModel = viewModel
                        )
                    }

                    // Pantalla de Actualizar Dispositivo
                    composable(
                        route = "actualizar_dispositivo/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        ActualizarDispositivoScreen(
                            idDispositivo = id,
                            onBack = { navController.popBackStack() },
                            onNavigate = { route -> navController.navigate(route) },
                            viewModel = viewModel
                        )
                    }

                    // Pantalla de Historial de Consumo
                    composable(
                        route = "historial_consumo/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        HistorialConsumoScreen(
                            idDispositivo = id,
                            onBack = { navController.popBackStack() },
                            onNavigate = { route -> navController.navigate(route) },
                            viewModel = viewModel
                        )
                    }

                    // Pantalla de Alertas
                    composable(
                        route = "alertas_dispositivo/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        AlertasScreen(
                            idDispositivo = id,
                            onBack = { navController.popBackStack() },
                            onConfigClick = { navController.navigate("config_consumo/$id") },
                            onNavigate = { route -> navController.navigate(route) },
                            viewModel = viewModel
                        )
                    }

                    // Pantalla de Configuración de Consumo Máximo
                    composable(
                        route = "config_consumo/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        ConfiguracionConsumoScreen(
                            idDispositivo = id,
                            onBack = { navController.popBackStack() },
                            onNavigate = { route -> navController.navigate(route) },
                            viewModel = viewModel
                        )
                    }

                    // Pantalla de Edición (Recibe argumentos)
                    composable(
                        route = "nuevo_dispositivo/{id}/{grupoNombre}/{grupoId}",
                        arguments = listOf(
                            navArgument("id") { type = NavType.StringType },
                            navArgument("grupoNombre") { type = NavType.StringType },
                            navArgument("grupoId") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        val grupoId = backStackEntry.arguments?.getString("grupoId") ?: ""

                        NuevoDispositivoScreen(
                            idExistente = id,
                            grupoIdInicial = grupoId,
                            listaGrupos = listaGrupos,
                            onBack = { navController.popBackStack() },
                            onNavigate = { route -> navController.navigate(route) },
                            onSaveClick = { nuevoNombre, nuevoGrupoId, nuevoTipo ->
                                // AQUÍ OCURRE EL UPDATE REAL EN LA BASE DE DATOS
                                scope.launch {
                                    val entidadActualizada = DispositivoEntity(
                                        id_dispositivo = id,
                                        nombre = nuevoNombre,
                                        tipo = nuevoTipo,
                                        estado = true,
                                        consumo_actual = 0.0,
                                        id_gateway = null,
                                        id_grupo = nuevoGrupoId
                                    )
                                    repository.update(entidadActualizada)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}