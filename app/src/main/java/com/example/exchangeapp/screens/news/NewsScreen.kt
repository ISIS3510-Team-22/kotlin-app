package com.example.exchangeapp.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun NewsScreen(
    open: (String) -> Unit,
    modifier : Modifier = Modifier
){

    Column (
        modifier.fillMaxSize()
            .background(Color(0xFF0F3048))
    ) {
        Text("iniciada")
    }

}