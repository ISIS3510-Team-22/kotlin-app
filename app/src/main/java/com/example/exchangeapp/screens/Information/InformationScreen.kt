package com.example.exchangeapp.screens.Information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun InformationScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InformationViewModel = hiltViewModel()
){
    val buttontexts = listOf("Cooking & recipes while abroad", "Mental Health", "Adapting to a new city", "Universities info", "Current exchanges available")

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(Color("#0F3048".toColorInt()))
    ){
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ){
            IconButton(
                onClick = {viewModel.onMenuClick(openAndPopUp)},
                modifier =modifier
                    .size(100.dp)
                    .padding(25.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu, // Using the built-in menu icon
                    contentDescription = "",
                    modifier = modifier
                        .size(60.dp)
                )
            }
            Spacer(modifier = modifier.width(60.dp))

            Text("INFORMATION", modifier = modifier.padding(vertical = 15.dp).align(Alignment.CenterVertically), style = MaterialTheme.typography.headlineLarge )

            Spacer(modifier = modifier.width(65.dp))

            Icon(
                imageVector = Icons.Filled.CalendarToday,
                contentDescription = "",
                modifier = modifier
                    .size(80.dp)
                    .padding(vertical = 15.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        LazyColumn {
            items(buttontexts){ label ->
                LabeledButtons(label, viewModel.onChatClick(openAndPopUp), modifier)
        }
        }
        }
    }


@Composable
fun LabeledButtons(text : String, onClick : Unit, modifier: Modifier){
        Button(
            onClick = {onClick},
            shape = RoundedCornerShape(35),
            modifier = modifier
                .padding(horizontal = 20.dp, vertical = 15.dp)
                .fillMaxWidth()
                .height(120.dp),
            colors = ButtonColors(
                Color("#18354d".toColorInt()),
                Color.White,
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text(text = text, fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
}