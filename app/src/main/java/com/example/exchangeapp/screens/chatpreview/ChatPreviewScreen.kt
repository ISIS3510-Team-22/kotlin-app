package com.example.exchangeapp.screens.chatpreview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatPreviewScreen(onChatClick: (String) -> Unit) {
    val contacts = listOf(
        "John Doe",
        "Jane Smith",
        "Alice Johnson",
        "Bob Lee"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Chats",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(contacts) { contact ->
                ContactItem(contact = contact, onClick = { onChatClick(contact) })
            }
        }
    }
}

@Composable
fun ContactItem(contact: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = contact, style = MaterialTheme.typography.bodyMedium)
        Text(text = "Last message preview", style = MaterialTheme.typography.bodySmall)
    }
}