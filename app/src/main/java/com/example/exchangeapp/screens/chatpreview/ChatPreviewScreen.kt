package com.example.exchangeapp.screens.chatpreview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.exchangeapp.screens.chatpreview.ChatPreviewViewModel

@Composable
fun ChatPreviewScreen(
    open: (String) -> Unit ,
    viewModel: ChatPreviewViewModel = hiltViewModel()
) {
    val userNames by viewModel.userNames.collectAsState()

    LazyColumn {
        items(userNames) { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {

                        viewModel.getMessagesAndSetupChat(name, open)


                    }
            )
        }
    }
}

