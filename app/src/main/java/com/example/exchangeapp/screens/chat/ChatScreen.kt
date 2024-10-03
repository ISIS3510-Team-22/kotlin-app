package com.example.exchangeapp.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.exchangeapp.model.service.module.RequestLocationPermission

// Modelo para el mensaje
data class Message(val content: String, val isSentByUser: Boolean)

@Composable
fun ChatScreen(contactName: String) {
    RequestLocationPermission( { }, { }, { } )
    val messages = remember {
        mutableStateListOf(
            Message("Hello!", isSentByUser = true),
            Message("Hi! How are you?", isSentByUser = false),
            Message("I'm good, thanks for asking!", isSentByUser = true)
        )
    }
    var currentMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text(
            text = "Chat with $contactName",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true // Mostrar los mensajes desde el más reciente
        ) {
            items(messages) { message ->
                MessageBubble(message)
            }
        }

        // Campo de entrada de mensaje
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = currentMessage,
                onValueChange = { currentMessage = it },
                modifier = Modifier.weight(1f).padding(8.dp),
                placeholder = { Text("Type a message...") }
            )
            Button(
                onClick = {
                    if (currentMessage.isNotEmpty()) {
                        messages.add(Message(currentMessage, isSentByUser = true))
                        currentMessage = ""
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}


// Composable para mostrar los mensajes con diseño diferenciado
@Composable
fun MessageBubble(message: Message) {
    val alignment = if (message.isSentByUser) Arrangement.End else Arrangement.Start
    val bubbleColor = if (message.isSentByUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val textColor = if (message.isSentByUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = message.content,
                color = textColor,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


