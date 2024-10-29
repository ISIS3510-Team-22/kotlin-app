package com.example.exchangeapp.screens.information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.screens.TopBar

@Composable
fun InformationScreen(
    open: (String) -> Unit,
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InformationViewModel = hiltViewModel()
) {

    val labels = mapOf(
        "Cooking & recipes while abroad" to "recipes",
        "Mental Health" to "mental_health",
        "Adapting to a new city" to "adapting_tips",
        "Universities info" to "universities"
    )

    val buttontexts = listOf(
                "Cooking & recipes while abroad",
                "Mental Health",
                "Adapting to a new city",
                "Universities info",
                "Current exchanges available"
            )

    val sortedButtonInfo :List<Pair<String, Int>> = remember(viewModel.clickCounter) {
        buttontexts.map { label ->
            label to (viewModel.clickCounter[label] ?: 0)
        }.sortedByDescending { it.second }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
    ) {
        TopBar(onMenuClick = { viewModel.onMenuClick(open) }, screenTitle = "INFORMATION",
            icon = Icons.Default.CalendarToday, iconDescription = "Calendar", iconAction = {})

        LazyColumn(modifier = Modifier.padding(bottom = 68.dp)) {
            items(sortedButtonInfo) { item ->

                val label = item.first
                val count = item.second

                Spacer(Modifier.padding(5.dp))
                InfoButton(text = label, onButtonClick = { labels[label]?.let {
                    viewModel.updateButtonClick(label)
                    viewModel.onSubViewClick(it,open)
                } })
            }
        }
    }
}


@Composable
fun InfoButton(text: String, onButtonClick:()-> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onButtonClick,
        shape = RoundedCornerShape(25),
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
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
