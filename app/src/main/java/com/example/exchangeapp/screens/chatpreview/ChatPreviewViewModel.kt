package com.example.exchangeapp.screens.chatpreview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.CHAT_SCREEN
import com.example.exchangeapp.SIGN_UP_SCREEN
import com.example.exchangeapp.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.exchangeapp.model.service.UserRepository
import javax.inject.Inject
import com.example.exchangeapp.model.service.impl.ChatService
import com.example.exchangeapp.model.service.module.Message

@HiltViewModel
class ChatPreviewViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatService: ChatService,
    private val accountService: AccountService
) : ViewModel() {

    private val _userNames = MutableStateFlow<List<String>>(emptyList())
    val userNames: StateFlow<List<String>> = _userNames.asStateFlow()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()
    val currentUserId = accountService.currentUserId

    init {
        fetchUserNames()
    }

    private fun fetchUserNames() {
        viewModelScope.launch {
            userRepository.getUserNames()
                .collect { names ->
                    _userNames.value = names
                }
        }
    }





    fun getMessagesAndSetupChat(userName: String, onChatCreated:(String)->Unit) {
        viewModelScope.launch {
            // Obtiene el userId de la persona con la que se va a chatear
            val otherUserId = chatService.getUserIdByName(userName)
            Log.d("DINOSAURIO", currentUserId.toString())
            Log.d("DINOSAURIO", otherUserId.toString())
            if (currentUserId != null && otherUserId != null) {
                chatService.createChat(otherUserId, onChatCreated, userName, currentUserId)
                val chatId = chatService.getChatId(otherUserId)
                getMessages(chatId)
            }
                }
    }

    private fun getMessages(chatId: String) {
        viewModelScope.launch {
            chatService.getMessages(chatId).collect { chatMessages ->
                _messages.value = chatMessages
            }
        }
    }
}
