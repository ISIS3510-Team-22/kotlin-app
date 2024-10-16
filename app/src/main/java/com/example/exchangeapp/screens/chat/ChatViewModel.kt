package com.example.exchangeapp.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.impl.ChatService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.exchangeapp.model.service.module.Message
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatService: ChatService,
    private val accountService: AccountService
) : ViewModel() {

    val currentUserId = accountService.currentUserId
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()



    // Enviar un mensaje
    fun sendMessage(receiverName: String, messageText: String) {

        viewModelScope.launch {


            val receiverId = chatService.getUserIdByName(receiverName)
            val currentUserId = currentUserId


            if (currentUserId != null && receiverId != null) {
                val chatId = if (currentUserId!! < receiverId) "$currentUserId-$receiverId" else "$receiverId-$currentUserId"
                if (chatId != null) {
                    Log.d("CHAT", chatId)
                }
                val message = Message(
                    senderId = currentUserId!!,
                    receiverId = receiverId,
                    message = messageText,
                    timestamp = System.currentTimeMillis()
                )
                if (chatId != null) {
                    Log.d("TREX", currentUserId)
                    Log.d("TREX",receiverId)
                    chatService.sendMessage(chatId, message)
                }
            }
        }
    }

    fun getMessages(name: String) {
        viewModelScope.launch {
            val receiverId = chatService.getUserIdByName(name)
            val currentUserId = currentUserId
            val chatId = if (currentUserId!! < receiverId.toString()) "$currentUserId-$receiverId" else "$receiverId-$currentUserId"
            chatService.getMessages(chatId).collect { chatMessages ->
                _messages.value = chatMessages

                //Recupero los obj mensajes totalmente
            }
        }
    }
}