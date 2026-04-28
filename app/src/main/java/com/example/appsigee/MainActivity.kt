package com.example.appsigee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appsigee.ui.screens.dispositivos.DispositivosScreen
import com.example.appsigee.ui.screens.dispositivos.NuevoDispositivoScreen
import com.example.appsigee.ui.theme.AppSigeeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppSigeeTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "dispositivos"
                ) {
                    composable("dispositivos") {
                        DispositivosScreen(
                            onNuevoClick = { navController.navigate("nuevo_dispositivo") }
                        )
                    }
                    composable("nuevo_dispositivo") {
                        NuevoDispositivoScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
