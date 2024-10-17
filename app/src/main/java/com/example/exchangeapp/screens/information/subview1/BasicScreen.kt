package com.example.exchangeapp.screens.information.subview1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun BasicScreen(
    name : String,
    modifier : Modifier = Modifier,
    viewModel: BasicScreenViewModel = hiltViewModel()
) {

    var info by remember { mutableStateOf(emptyList<String>()) }

    viewModel.getDocument { title, description ->
        info = listOf(title, description)
    }

    var documentDataList by remember { mutableStateOf(emptyList<Map<String, Any>>()) }

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
        Text(labels[name].toString())
            documentDataList.forEach { documentData ->
                DisplayDocumentData(documentData)
                Spacer(modifier = Modifier.height(16.dp))
            }
    }
}

@Composable
fun DisplayDocumentData(documentData: Map<String, Any>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        documentData.forEach { (key, value) ->
            
            Text(text = "$key: $value", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}