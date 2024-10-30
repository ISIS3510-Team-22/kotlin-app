package com.example.exchangeapp.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.Message
import com.example.exchangeapp.screens.MessageBox

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    popUp: () -> Unit,
    receiverName: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val currentUserId = viewModel.currentUserId
    val currentMessage = viewModel.currentMessage.collectAsState()
    val isEnabled = viewModel.isEnabled.collectAsState()


    viewModel.getMessages(receiverName)

    val imeVisible = WindowInsets.isImeVisible

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .imePadding()
            .padding(start = 20.dp, end = 20.dp, bottom = if (!imeVisible) 5.dp else 0.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { popUp() },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = receiverName,
                color = Color.White,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.padding(end = 36.dp))

        }


        LazyColumn(
            modifier = Modifier.weight(1f),

            ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isSentByCurrentUser = message.senderId == currentUserId
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        MessageBox(
            currentMessage = currentMessage.value,
            updateMsgFun = { viewModel.updateCurrentMessage(it) },
            isEnabled = isEnabled.value,
            sendMsgFun = {receiverName, currentMessage ->
                viewModel.sendMessage(receiverName, currentMessage)
                viewModel.getMessages(receiverName)
            },
            receiverName = receiverName
        )
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
            style = MaterialTheme.typography.bodyLarge.copy(),
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
