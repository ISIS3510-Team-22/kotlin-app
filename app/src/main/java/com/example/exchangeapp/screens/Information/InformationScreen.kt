package com.example.exchangeapp.screens.Information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun InformationScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InformationViewModel = hiltViewModel()
){
    Column (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(Color("#0F3048".toColorInt())),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Row(verticalAlignment = Alignment.Top) {
            //Image( Icons.Rounded.Menu)
            Text("Information", style = MaterialTheme.typography.headlineMedium, modifier = modifier.padding(vertical = 10.dp))
        }
        Button( onClick = {viewModel.onChatClick(openAndPopUp)},
            shape = CutCornerShape(5),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            Text("Cooking & recipies while abroad")
        }
        Button( onClick = {viewModel.onChatClick(openAndPopUp)},
            shape = CutCornerShape(5),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            Text("Mental Health")
        }
    }
}