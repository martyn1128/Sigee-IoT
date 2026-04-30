package com.example.appsigee.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(currentRoute: String, onNavigate: (String) -> Unit) {
    val items = listOf("Saldo", "Dispositivos", "Recibos", "Reportes", "Ubícanos")
    val routes = listOf("saldo", "dispositivos", "recibos", "reportes", "ubicanos")
    val icons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Devices,
        Icons.Outlined.ReceiptLong,
        Icons.Outlined.ElectricBolt,
        Icons.Outlined.LocationOn
    )

    NavigationBar(
        containerColor = Color.White,
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
                selected = currentRoute == routes[index],
                onClick = { onNavigate(routes[index]) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF4CAF50),
                    selectedTextColor = Color(0xFF4CAF50),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
