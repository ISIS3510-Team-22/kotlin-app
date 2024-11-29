package com.example.exchangeapp.screens.information.subviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.ConnectionBackBox
import com.example.exchangeapp.screens.NoInternetBox
import com.example.exchangeapp.screens.connectivityStatus
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarScreen(
    name : String,
    modifier: Modifier = Modifier,
    viewModel: BasicScreenViewModel = hiltViewModel(),
    popUp : () -> Unit,
    open: (String) -> Unit){

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

    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val labels = mapOf(
        "universities" to "Universities info"
    )

    var documentDataList by remember { mutableStateOf(emptyList<Map<String, Any>>()) }

    // Fetch documents using the ViewModel
    viewModel.getDocumentsData(name) { fetchedDataList ->
        documentDataList = fetchedDataList
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
    ) {
        NoInternetBox(connectionAvailable)
        ConnectionBackBox(showConnectionRestored.value)
        Spacer(Modifier.height(1.dp))
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
            Text(labels[name].toString(), style = MaterialTheme.typography.headlineMedium, color = Color.White)
        }
        SearchBar(
            modifier = modifier
                .align(Alignment.CenterHorizontally).semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = text,
                    onQueryChange = {
                        text = it
                    },
                    onSearch = { expanded = false },
                    expanded = expanded,
                    onExpandedChange = { expanded = false },
                    placeholder = { Text("Search") },
                    trailingIcon = { Icon(Icons.Outlined.FilterAlt, contentDescription = null) },
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            colors = SearchBarDefaults.colors(
                containerColor = Color.Gray
            )
        ) {
            }
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
                ),
                shape = RoundedCornerShape(30)
            ) {
                DisplayDocumentData2( documentData = documentData,viewModel,open)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun DisplayDocumentData2(documentData: Map<String, Any>, viewModel: BasicScreenViewModel,
                         open: (String) -> Unit) {

    var isExpanded by remember { mutableStateOf(false) }

    // Extract the title from the document data
    val titleOrName = documentData["title"]?.toString() ?: documentData["name"]?.toString() ?: "No Title or Name"


    val details = documentData.filterKeys { it !in listOf("title", "name","createdAt") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextButton(onClick = {viewModel.onUniversityClick(titleOrName, open)
            }) {
                Text(
                    text = titleOrName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f),
                    color = Color.White
                )
            }


            ExpandableButton(
                expanded = isExpanded,
                onClick = { isExpanded = !isExpanded } // Toggle the state
            )
        }

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