package com.example.appsigee.ui.screens.saldo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsigee.ui.screens.components.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaldoScreen(onNavigate: (String) -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Saldo",
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
            BottomNavBar(currentRoute = "saldo", onNavigate = onNavigate)
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

            Spacer(modifier = Modifier.height(24.dp))

            // Información de periodo
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = buildAnnotatedString {
                        append("Periodo: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Febrero - Abril 2026")
                        }
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = buildAnnotatedString {
                        append("Fecha límite de pago: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("19/Abril/2026")
                        }
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = buildAnnotatedString {
                        append("Estatus: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))) {
                            append("Pagado")
                        }
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text("Saldo actual:", fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Text("$0", fontSize = 64.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))

            Spacer(modifier = Modifier.height(32.dp))

            Text("193 kWh consumo Medio", fontSize = 14.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(8.dp))

            // Barra de consumo (Green to Red)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF8BC34A), Color(0xFFCDDC39), Color(0xFFFFEB3B), Color(0xFFFF9800), Color(0xFFF44336))
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
            ) {
                // Indicador actual
                Box(
                    modifier = Modifier
                        .padding(start = 120.dp) // Proporcional al consumo
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(Color.Black)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5D6A7)), // Light green as in image
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Pagar", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Simula tu consumo", color = Color(0xFF008941), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
