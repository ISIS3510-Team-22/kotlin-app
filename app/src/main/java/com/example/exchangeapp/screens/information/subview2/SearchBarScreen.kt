package com.example.exchangeapp.screens.information.subview2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(
    name : String,
    modifier: Modifier = Modifier,
    viewModel: SearchBarScreenViewModel = hiltViewModel(),
    popUp : () -> Unit
){

    val labels = mapOf(
        "universities" to "Universities info"
    )
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
    ) {
        Row {
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
            Text(labels[name].toString(), style = MaterialTheme.typography.headlineMedium)
        }
    }
}