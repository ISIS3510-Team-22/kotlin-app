package com.example.exchangeapp.screens.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.screens.MessageBox
import com.example.exchangeapp.screens.MessageBubble
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
    val messages by viewModel.messages.collectAsState()
    val currentUserId = viewModel.currentUserId

    viewModel.getMessages()

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

        val listState = rememberLazyListState()

        LaunchedEffect(messages) {
            if (messages.isNotEmpty()) {
                listState.scrollToItem(messages.size - 1)
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState

            ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isSentByCurrentUser = message.senderId == currentUserId
                )
            }
        }


        MessageBox(
            currentMessage = currentMessage.value,
            updateMsgFun = { viewModel.updateCurrentMessage(it) },
            isEnabled = isEnabled.value,
            isAiChat = true,
            sendMsgFunAI = { viewModel.sendMessage(it)
            viewModel.getMessages()}
        )
    }
}