package com.example.exchangeapp.screens.universities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.ConnectionBackBox
import com.example.exchangeapp.screens.NoInternetBox
import com.example.exchangeapp.screens.connectivityStatus
import kotlinx.coroutines.delay

@Composable
fun UniversityScreen(
    university : String,
    open: (String) -> Unit,
    modifier : Modifier = Modifier,
    viewModel: UniversityViewModel = hiltViewModel(),
    popUp: () -> Unit
){
    val connectionAvailable = connectivityStatus().value == ConnectionStatus.Available
    var wasConnectionAvailable = remember { mutableStateOf(true) }
    var showConnectionRestored = remember { mutableStateOf(false) }

    LaunchedEffect(connectionAvailable) {
        if (connectionAvailable) {
            if (connectionAvailable && !wasConnectionAvailable.value) {
                showConnectionRestored.value = true
                delay(2000)
                showConnectionRestored.value = false
            }
            wasConnectionAvailable.value = connectionAvailable
        }
    }



    Column (
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        NoInternetBox(connectionAvailable)
        ConnectionBackBox(showConnectionRestored.value)
        Spacer(Modifier.height(1.dp))
        Row {
            IconButton(
                onClick = { popUp() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    "",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }
            Text(university, style = MaterialTheme.typography.headlineMedium, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aca va la info de la universidad (country, city, students)")
        Button(onClick = {viewModel.displayDetils(university,open)}) {
            Text("Comments",style = MaterialTheme.typography.headlineMedium, color = Color.White )
        }
    }
}
