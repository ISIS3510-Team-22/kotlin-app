package com.example.exchangeapp.screens.aichat

import androidx.lifecycle.ViewModel
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.model.service.module.Message
import kotlinx.coroutines.flow.MutableStateFlow

class AiChatViewModel: ViewModel() {

    var currentMessage = MutableStateFlow("")
    var isEnabled = MutableStateFlow(false)

    fun updateCurrentMessage(newMessage: String) {
        currentMessage.value = newMessage
        isEnabled.value = newMessage.isNotEmpty()
    }

    fun sendMessage(message: String) {
        // Send message to AI using http request

    }

    fun onMenuClick(open: (String) -> Unit) {
        open(MENU_SCREEN)
    }
}