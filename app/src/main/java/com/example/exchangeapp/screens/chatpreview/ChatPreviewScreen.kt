package com.example.exchangeapp.screens.chatpreview

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.TopBar
import com.example.exchangeapp.screens.connectivityStatus
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.delay


@Composable
fun ChatPreviewScreen(
    open: (String) -> Unit,
    viewModel: ChatPreviewViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    var userList = viewModel.userList.collectAsState()
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("PERMISSION", if (isGranted) "Got permission" else "Permission denied")
    }
    val connectionAvailable = connectivityStatus().value == ConnectionStatus.Available
    val context = LocalContext.current
    var showConnectionRestored = remember { mutableStateOf(false) }


    ToastListener(viewModel)

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        Log.d("PERMISSION", "Launched permission overlay")
    }

    LaunchedEffect(users) {

        if (userList.value.isEmpty()) {
            viewModel.updateUserList(users.toList())
        }
    }


    LaunchedEffect(connectionAvailable) {
        if (connectionAvailable) {
            showConnectionRestored.value = true
            delay(2000)
            showConnectionRestored.value = false
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
                    viewModel.updateUserList(updatedUsers)
                }
            })

        viewModel.updateInfo(connectionAvailable, context)
        Log.d("TREX", "Connection status: $connectionAvailable")
        if (connectionAvailable){
            viewModel.saveSnapshotToCache(context)
        }



        if (users.isEmpty()) {
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
                items(userList.value) { user ->
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

@Composable
fun ToastListener(viewModel: ChatPreviewViewModel) {
    val context = LocalContext.current


    LaunchedEffect(viewModel.errorMessage.value) {
        viewModel.errorMessage.value.let { message ->
            if (message.isNotEmpty()) {


                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                // Clear the error message after showing the toast
                viewModel.errorMessage.value = ""

            }
        }
    }
}
