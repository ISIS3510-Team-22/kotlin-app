package com.example.exchangeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun InformationScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier,
    viewModel: InformationViewModel = hiltViewModel()
){

    Column (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(Color("#0F3048".toColorInt())),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){

        Button( onClick = {viewModel.onChatClick(openAndPopUp)},
            shape = CutCornerShape(20) ) {

        }
        TextButton(onClick = {viewModel.onChatClick(openAndPopUp)} ) {
            Text("I hope this shows")
        }
    }
}