package com.example.exchangeapp.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.screens.chat.ChatViewModel

@Composable
fun ChatScreen(
    receiverName: String,
    viewModel: ChatViewModel = hiltViewModel()
) {


    val messages by viewModel.messages.collectAsState()
    var currentMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                Text(text = message.message)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            BasicTextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                modifier = Modifier.weight(1f).padding(8.dp)
            )
            Button(
                onClick = {
                    viewModel.sendMessage(receiverName, currentMessage)
                    currentMessage = ""
                }
            ) {
                Text("Send")
            }
        }
    }
}
