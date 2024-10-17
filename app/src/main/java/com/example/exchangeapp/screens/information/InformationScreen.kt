package com.example.exchangeapp.screens.information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun InformationScreen(
    open: (String) -> Unit,
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InformationViewModel = hiltViewModel()
) {
    val buttontexts = listOf(
        "Cooking & recipes while abroad",
        "Mental Health",
        "Adapting to a new city",
        "Universities info",
        "Current exchanges available"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.onMenuClick(open) },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }
            Text(
                text = "INFORMATION",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(
                onClick = {},
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }


        LazyColumn (modifier= Modifier.padding(bottom = 68.dp)){
            items(buttontexts) { label ->
                LabeledButtons(label, viewModel.onChatClick(openAndPopUp), modifier)
                Spacer(Modifier.padding(15.dp))
            }
        }
    }
}


@Composable
fun LabeledButtons(text: String, onClick: Unit, modifier: Modifier) {
    Button(
        onClick = { onClick },
        shape = RoundedCornerShape(35),
        modifier = modifier
            .padding()
            .fillMaxWidth()
            .height(110.dp),
        colors = ButtonColors(
            Color(0xFF18354d),
            Color.White,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.onTertiary
        )
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}