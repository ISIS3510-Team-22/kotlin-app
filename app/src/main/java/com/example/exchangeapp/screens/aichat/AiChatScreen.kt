package com.example.exchangeapp.screens.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.screens.TopBar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiChatScreen(
    open: (String) -> Unit,
    viewModel: AiChatViewModel = hiltViewModel()
) {
//    val messages by viewModel.messages.collectAsState()
//    val currentUserId = viewModel.currentUserId
    val currentMessage = viewModel.currentMessage.collectAsState()
    val isEnabled = viewModel.isEnabled.collectAsState()

//    viewModel.getMessages(receiverName)
    val imeVisible = WindowInsets.isImeVisible


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
                //TODO Add action to button
            })

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentMessage.value,
                onValueChange = { viewModel.updateCurrentMessage(it) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = MaterialTheme.shapes.large,
                placeholder = { Text("Write a message...", color = Color(0xFFE8E8E8)) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    selectionColors = TextSelectionColors(
                        handleColor = Color.White,
                        backgroundColor = Color.LightGray
                    )
                )
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFFFFFFF))
            ) {
                IconButton(
                    onClick = {
                        viewModel.sendMessage(currentMessage.value)
//                        viewModel.getMessages(receiverName)
                    },
                    enabled = isEnabled.value
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        "Send button",
                        tint = if (isEnabled.value) Color(0xFF0F3048) else Color.Gray
                    )
                }
            }
        }
    }
}