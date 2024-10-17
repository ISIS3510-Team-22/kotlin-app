package com.example.exchangeapp.screens.information.subview1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun BasicScreen(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: BasicScreenViewModel = hiltViewModel()
) {

    var expanded by remember { mutableStateOf(false) }

    var documentDataList by remember { mutableStateOf(emptyList<Map<String, Any>>()) }

    // Fetch documents using the ViewModel
    viewModel.getDocumentsData(name) { fetchedDataList ->
        documentDataList = fetchedDataList
    }

    val labels = mapOf(
        "recipes" to "Cooking & recipes while abroad",
        "mental_health" to "Mental Health",
        "adapting_tips" to "Adapting to a new city"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
    ) {
        Text(labels[name].toString(), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        documentDataList.forEach { documentData ->
            Card (
                modifier = modifier
                    .background(Color(0xFF18354d)),
                colors = CardColors(
                    Color(0xFF18354d),
                    Color.White,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.onTertiary
                )
            ) {
                DisplayDocumentData( documentData = documentData)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DisplayDocumentData(documentData: Map<String, Any>) {
    // State to track whether the details are expanded
    var isExpanded by remember { mutableStateOf(false) }

    // Extract the title from the document data
    val titleOrName = documentData["title"]?.toString() ?: documentData["name"]?.toString() ?: "No Title or Name"

    // Extract the remaining details, excluding the title and name
    val details = documentData.filterKeys { it !in listOf("title", "name") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Render the title in a Row with an expandable button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = titleOrName,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            // Expandable button
            expandableButton(
                expanded = isExpanded,
                onClick = { isExpanded = !isExpanded } // Toggle the state
            )
        }

        // Conditionally render the details only if the section is expanded
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                details.forEach { (key, value) ->
                    Text(text = "$key: $value", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun expandableButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.ExpandMore,
            contentDescription = if (expanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}