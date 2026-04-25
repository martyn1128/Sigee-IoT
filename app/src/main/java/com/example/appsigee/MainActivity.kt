package com.example.appsigee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.appsigee.ui.screens.dispositivos.DispositivosScreen // Importante: Importa tu pantalla
import com.example.appsigee.ui.theme.AppSigeeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Aplicamos el tema personalizado de tu app (donde están tus colores de SIGEE)
            AppSigeeTheme {
                // Ya no necesitamos el Scaffold aquí porque DispositivosScreen ya tiene su propio Scaffold
                DispositivosScreen()
            }
        }
    }
}