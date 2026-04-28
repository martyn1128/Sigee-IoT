package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoDispositivoScreen(onBack: () -> Unit) {
    var id by remember { mutableStateOf("") }
    var grupo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }

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
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Saldo") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Devices, contentDescription = "Dispositivos") },
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
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Reportes") },
                    label = { Text("Reportes") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Ubícanos") },
                    label = { Text("Ubícanos") }
                )
            }
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

            // ID Field
            FormField(label = "ID", value = id, onValueChange = { id = it }, placeholder = "ab3d7j")
            
            Spacer(modifier = Modifier.height(16.dp))

            // Grupo Field
            FormField(
                label = "Grupo:", 
                value = grupo, 
                onValueChange = { grupo = it }, 
                placeholder = "Grupo1",
                trailingIcon = {
                    Icon(
                        Icons.Default.CheckCircle, 
                        contentDescription = null, 
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre Field
            FormField(label = "Nombre:", value = nombre, onValueChange = { nombre = it }, placeholder = "NuevoDispositivo")

            Spacer(modifier = Modifier.height(24.dp))

            // Imagen Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    "Imagen:",
                    modifier = Modifier.width(80.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                
                Box(contentAlignment = Alignment.TopEnd) {
                    Surface(
                        modifier = Modifier.size(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        color = Color.Transparent
                    ) {
                        Icon(
                            Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.padding(16.dp).size(80.dp),
                            tint = Color.Black
                        )
                    }
                    
                    Icon(
                        Icons.Default.AddCircleOutline,
                        contentDescription = "Añadir imagen",
                        tint = Color.Black,
                        modifier = Modifier
                            .offset(x = 10.dp, y = (-10).dp)
                            .size(28.dp)
                            .background(Color.White, shape = RoundedCornerShape(14.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            modifier = Modifier.width(80.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = trailingIcon,
            singleLine = true
        )
    }
}
