package com.example.appsigee.ui.screens.ubicanos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsigee.ui.screens.components.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbicanosScreen(onNavigate: (String) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Ubícanos",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .background(Color(0xFF008941), CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(currentRoute = "ubicanos", onNavigate = onNavigate)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Placeholder for Google Maps
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE8F5E9)), // Light green background for the map area
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Mapa de ubicación", color = Color.Gray)
                Text("Placeholder de Google Maps", color = Color.Gray, fontSize = 12.sp)
            }

            // Map UI Elements (Simulated)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                Card(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, tint = Color.Gray)
                    }
                }
            }
        }
    }
}
