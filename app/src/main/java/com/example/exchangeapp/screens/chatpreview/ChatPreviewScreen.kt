package com.example.exchangeapp.screens.chatpreview

import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.exchangeapp.model.service.User


@Composable
fun ChatPreviewScreen(
    open: (String) -> Unit,
    viewModel: ChatPreviewViewModel = hiltViewModel()
) {
    val userNames by viewModel.userNames.collectAsState()
    val users by viewModel.users.collectAsState()
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("PERMISSION", if (isGranted) "Got permission" else "Permission denied")
    }
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        Log.d("PERMISSION", "Launched permission overlay")
    }

    LaunchedEffect(users) {

        if (userList.isEmpty()) {
            userList = users.toList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.onMenuClick(open) },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }
            Text(
                text = "CHAT",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = {
                    viewModel.handleLocationUpdate(users) { updatedUsers ->
                        userList = updatedUsers
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        if (userNames.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xFF0F3048))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(bottom = 65.dp, top = 10.dp)
            ) {
                items(userList) { user ->
                    Card(
                        onClick = { viewModel.getMessagesAndSetupChat(user.name, open) },
                        elevation = CardDefaults.cardElevation(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF18354d)
                        ),
                        modifier = Modifier.wrapContentHeight(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .height(70.dp)
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(Modifier.padding(10.dp))

                }
            }
        }
    }
}
