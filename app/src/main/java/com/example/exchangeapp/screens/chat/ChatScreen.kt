package com.example.exchangeapp.screens.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.Message

@Composable
fun ChatScreen(
    receiverName: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val currentUserId = viewModel.currentUserId
    var currentMessage by remember { mutableStateOf("") }
    viewModel.getMessages(receiverName)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF0F3048))
        .padding(16.dp)
    ) {



        Text(
            text = receiverName,
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isSentByCurrentUser = message.senderId == currentUserId
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                shape = MaterialTheme.shapes.large, // Bordes circulares
                placeholder = { Text("Write a message...") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray
                )
            )
            Button(
                onClick = {
                    viewModel.sendMessage(receiverName, currentMessage)
                    currentMessage = ""
                    viewModel.getMessages(receiverName)
                    Log.d("CHAT", "Pantalla Chat")
                    Log.d("CHAT", messages.toString())
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .height(56.dp)
                    .width(90.dp),
            ) {
                Text(
                    text = "Send",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold)
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
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = if (isSentByCurrentUser) Color.White else Color.Black,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .background(
                    color = if (isSentByCurrentUser) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(10.dp)
        )
    }
}
