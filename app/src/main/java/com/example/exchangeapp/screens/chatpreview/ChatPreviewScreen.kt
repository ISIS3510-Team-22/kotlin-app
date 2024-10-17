package com.example.exchangeapp.screens.chatpreview

import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.User
import com.example.exchangeapp.screens.TopBar
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource


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
        TopBar(onMenuClick = { viewModel.onMenuClick(open) }, screenTitle = "CHAT",
            icon = Icons.Default.LocationOn, iconDescription = "Location", iconAction = {
                viewModel.handleLocationUpdate(users) { updatedUsers ->
                    userList = updatedUsers
                }
            })

        if (userNames.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xFF0F3048))
                    .padding(bottom = 85.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DotLottieAnimation(
                    source = DotLottieSource.Asset("plane_loading.lottie"),
                    autoplay = true,
                    loop = false,
                    speed = 3f,
                    useFrameInterpolation = false,
                    modifier = Modifier.scale(0.7f)
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
