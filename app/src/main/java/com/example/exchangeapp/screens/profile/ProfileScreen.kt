package com.example.exchangeapp.screens.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.exchangeapp.CAMERA_SCREEN
import com.example.exchangeapp.R
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.connectivityStatus

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    popUp: () -> Unit,
    open: (String) -> Unit,
) {
    val user by viewModel.currentUser.collectAsState()
    val connectionAvailable = connectivityStatus().value == ConnectionStatus.Available

    LaunchedEffect(connectionAvailable) {
        viewModel.setConnectivityStatus(connectionAvailable)
    }

    // Reload user data whenever this screen is recomposed (e.g., when coming back from camera)
    LaunchedEffect(key1 = true) {
        viewModel.loadUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { popUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(0.8f))
            Text(
                text = "PROFILE",
                fontSize = 40.sp,
                color = Color.White,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.weight(1.2f))
        }

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .size(250.dp) // Outer box for layout
                .padding(4.dp)
        ) {
            // Circular clipped box for the image
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                // Display user's profile picture or default icon
                if (user?.profilePictureUrl != null && user!!.profilePictureUrl != "") {
                    Image(
                        painter = rememberAsyncImagePainter(model = user?.profilePictureUrl),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(248.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.user),
                        contentDescription = "Default User Icon",
                        modifier = Modifier.size(248.dp)
                    )
                }
            }
            Box(                    modifier = Modifier
                .align(Alignment.BottomEnd) // Align to bottom-right of the outer box
                .offset(
                    x = (-15).dp,
                    y = (-8).dp
                ) // Slight offset to position outside the circle
                .size(48.dp)
                .background(Color.White, shape = CircleShape) // Circular background
                .padding(8.dp)) {


                // Place the button partially outside the circle
                IconButton(
                    onClick = { open(CAMERA_SCREEN) },
                    enabled = connectionAvailable,

                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt, // Camera icon
                        contentDescription = "Change Profile Picture",
                        tint = Color(0xFF0F3048)
                    )
                }
            }
            var context = LocalContext.current
            if (!connectionAvailable) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            Toast
                                .makeText(context, "Cannot update profile picture, check your connection", Toast.LENGTH_SHORT)
                                .show()
                        }
                )
            }
        }





        Spacer(modifier = Modifier.height(76.dp))

        // User Name
        user?.let {
            Text(
                text = it.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // User Email
        user?.let {
            Text(
                text = it.email,
                fontSize = 24.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location (Latitude and Longitude)
        Text(
            text = "Location: ${user?.lat}, ${user?.long}",
            fontSize = 16.sp,
            color = Color.White
        )
    }
}
