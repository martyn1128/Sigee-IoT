package com.example.appsigee.ui.screens.recibos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
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
fun RecibosScreen(onNavigate: (String) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Recibos",
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
            BottomNavBar(currentRoute = "recibos", onNavigate = onNavigate)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Selector de servicio
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(width = 0.5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Alias - Número de servicio", fontSize = 12.sp, color = Color.Gray)
                        Text("casa - 173931200838", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(32.dp))

            // Información del último periodo
            Column(modifier = Modifier.fillMaxWidth()) {
                Row {
                    Text("Último periodo: ", color = Color.Gray, fontSize = 16.sp)
                    Text("Febrero - Abril 2026", fontWeight = FontWeight.Bold, color = Color(0xFF1A237E), fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text("Fecha límite de pago: ", color = Color.Gray, fontSize = 14.sp)
                    Text("19/Abril/2026", color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text("Importe: ", color = Color.Gray, fontSize = 14.sp)
                    Text("$306", color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text("Estatus: ", color = Color.Gray, fontSize = 14.sp)
                    Text("Pagado", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { }
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF008941), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Ver recibo", color = Color(0xFF008941), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
