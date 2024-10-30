package com.example.exchangeapp.screens.aichat

import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.Message
import com.example.exchangeapp.screens.MessageBox
import com.example.exchangeapp.screens.TopBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiChatScreen(
    open: (String) -> Unit,
    viewModel: AiChatViewModel = hiltViewModel()
) {
    val currentMessage = viewModel.currentMessage.collectAsState()
    val isEnabled = viewModel.isEnabled.collectAsState()
    val imeVisible = WindowInsets.isImeVisible
    val sentMessage = viewModel.sentMessage.collectAsState()
    val aiResponse = viewModel.aiResponse.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(
                bottom = if (!imeVisible) 110.dp else 0.dp,
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
            .imePadding()
    ) {
        TopBar(onMenuClick = { viewModel.onMenuClick(open) }, screenTitle = "AI HELPER",
            icon = Icons.Default.CleaningServices, iconDescription = "Location", iconAction = {
                viewModel.resetMessages()
            })
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()).weight(1f)
        ) {
            MessageBubble(
                message = Message(
                    message = "Hello, I am AI ChatBot. How can I help you?",
                    senderId = "AI",
                    receiverId = "User",
                    timestamp = System.currentTimeMillis()
                ),
                isSentByCurrentUser = false
            )
            if (sentMessage.value.isNotEmpty()) {
                MessageBubble(
                    message = Message(
                        message = sentMessage.value,
                        senderId = "User",
                        receiverId = "AI",
                        timestamp = System.currentTimeMillis()
                    ),
                    isSentByCurrentUser = true
                )
            }
            if (aiResponse.value.isNotEmpty()) {
                MessageBubble(
                    message = Message(
                        message = aiResponse.value.toString(),
                        senderId = "AI",
                        receiverId = "User",
                        timestamp = System.currentTimeMillis()
                    ),
                    isSentByCurrentUser = false
                )
            }
        }


        MessageBox(
            currentMessage = currentMessage.value,
            updateMsgFun = { viewModel.updateCurrentMessage(it) },
            isEnabled = isEnabled.value,
            isAiChat = true,
            sendMsgFunAI = { viewModel.sendMessage(it) }
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