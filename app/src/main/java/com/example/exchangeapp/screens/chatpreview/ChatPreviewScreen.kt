package com.example.exchangeapp.screens.chatpreview

import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun ChatPreviewScreen(
    open: (String) -> Unit,
    viewModel: ChatPreviewViewModel = hiltViewModel()
) {
    val userNames by viewModel.userNames.collectAsState()
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Si se otorgan los permisos, obtenemos la ubicación
            Log.d("PERMISSION", "Hay permiso")
        } else {
            // Manejo en caso de que el permiso sea denegado
            Log.d("PERMISSION", "Permiso de ubicación denegado")
        }
    }
    var currentLocation by remember {mutableStateOf<Location?>(null)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048)) // Fondo más oscuro
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                viewModel.fetchCurrentLocation { location ->
                    currentLocation = location // Almacena la ubicación obtenida
                    if (location != null) {
                        Log.d("PERMISSION", "Ubicación obtenida Screen: ${location.latitude}, ${location.longitude}")
                    }

                }
            }) {
                Text("Get Current Location")
            }
            // Título "Chats"
            Text(
                text = "Chats",
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            // Botón de ubicación con ícono
            IconButton(
                onClick = {locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)}, // Acción a realizar cuando se presiona el botón
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Ubicación",
                    tint = Color.White, // Color blanco para el icono
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // Lista de chats
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp) // Espacio entre los elementos
        ) {
            items(userNames) { name ->
                // Caja para cada usuario
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color(0xFF3A506B)) // Fondo de cada caja
                        .clickable {
                            viewModel.getMessagesAndSetupChat(name, open)
                        }
                        .padding(16.dp), // Espaciado interno
                    contentAlignment = Alignment.Center // Centra el texto dentro de la caja
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
