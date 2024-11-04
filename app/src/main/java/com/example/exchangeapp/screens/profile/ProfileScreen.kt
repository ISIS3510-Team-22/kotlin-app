package com.example.exchangeapp.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.exchangeapp.R

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    popUp : () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()





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
        ){
            IconButton(
                onClick = {popUp() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    "",
                    modifier = Modifier
                        .size(60.dp),
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
                .size(250.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .padding(4.dp)
                .clip(CircleShape)

        ) {
            if (user?.profilePictureUrl != null && user!!.profilePictureUrl != ""){
                Image(
                    painter = rememberAsyncImagePainter(model = user?.profilePictureUrl),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(248.dp)
                        .clip(CircleShape)
                )
            }
            else
            {
                Image(
                    painter = painterResource(R.drawable.user),
                    contentDescription = "Default User Icon",
                    modifier = Modifier.size(348.dp)
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


