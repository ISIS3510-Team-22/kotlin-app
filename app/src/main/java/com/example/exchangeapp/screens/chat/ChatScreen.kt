package com.example.exchangeapp.screens.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.Message
import com.example.exchangeapp.screens.chatpreview.ChatPreviewViewModel

@Composable
fun ChatScreen(
    receiverName: String,
    viewModel: ChatViewModel = hiltViewModel(),
    viewModel2: ChatPreviewViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val currentUserId = viewModel.currentUserId
    var currentMessage by remember { mutableStateOf("") }
    viewModel.getMessages(receiverName)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isSentByCurrentUser = message.senderId == currentUserId
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            BasicTextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    viewModel.sendMessage(receiverName, currentMessage)
                    currentMessage = ""
                    viewModel.getMessages(receiverName)
                    Log.d("CHAT", "Pantalla Chat")
                    Log.d("CHAT", messages.toString())
                }
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isSentByCurrentUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .wrapContentWidth(if (isSentByCurrentUser) Alignment.End else Alignment.Start)
    ) {
        Text(
            text = message.message,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSentByCurrentUser) Color.White else Color.Black,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .background(
                    color = if (isSentByCurrentUser) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(8.dp)
        )
    }
}
