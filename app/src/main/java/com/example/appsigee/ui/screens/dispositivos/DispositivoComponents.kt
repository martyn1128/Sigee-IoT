package com.example.appsigee.ui.screens.dispositivos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appsigee.domain.model.TipoDispositivo
import com.example.appsigee.ui.utils.getIconForTipo

@Composable
fun SeccionImagenSeleccionable(tipo: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text("Imagen:", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.clickable { onClick() }
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Gray),
                color = Color.Transparent
            ) {
                if (tipo.startsWith("content://") || tipo.startsWith("file://")) {
                    AsyncImage(
                        model = tipo,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = getIconForTipo(tipo),
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp).size(80.dp),
                        tint = Color.Gray
                    )
                }
            }
            Icon(
                Icons.Default.Edit,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .offset(x = 8.dp, y = (-8).dp)
                    .size(28.dp)
                    .background(Color.White, shape = RoundedCornerShape(14.dp))
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun ImagePickerDialog(
    onDismiss: () -> Unit,
    onTipoSelected: (String) -> Unit,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona una imagen") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onCameraClick() }
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Cámara", tint = Color(0xFF008542), modifier = Modifier.size(48.dp))
                        Text("Cámara", fontSize = 12.sp)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onGalleryClick() }
                    ) {
                        Icon(Icons.Default.Photo, contentDescription = "Galería", tint = Color(0xFF008542), modifier = Modifier.size(48.dp))
                        Text("Galería", fontSize = 12.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(TipoDispositivo.entries.filter { it != TipoDispositivo.NUEVO }) { tipo ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onTipoSelected(tipo.name) }
                        ) {
                            Icon(
                                imageVector = getIconForTipo(tipo.name),
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = Color.Gray
                            )
                            Text(tipo.name, fontSize = 8.sp, maxLines = 1)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        TextField(
            value = value,
            onValueChange = { if (!readOnly) onValueChange(it) },
            modifier = Modifier.weight(1f),
            readOnly = readOnly,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F1F1),
                unfocusedContainerColor = Color(0xFFF1F1F1),
                disabledContainerColor = Color(0xFFEBEBEB),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )
    }
}
