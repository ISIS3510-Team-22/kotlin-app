package com.example.exchangeapp.screens.aichat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.impl.ChatService
import com.example.exchangeapp.model.service.module.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.cdimascio.dotenv.dotenv
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val chatService: ChatService,
    private val accountService: AccountService
) : ViewModel() {

    val dotenv = dotenv {
        directory = "/assets"
        filename = "env" // instead of '.env', use 'env'
    }

    val currentUserId = accountService.currentUserId
    var currentMessage = MutableStateFlow("")
    var isEnabled = MutableStateFlow(false)
    var sentMessage = MutableStateFlow("")
    var aiResponse = MutableStateFlow("")

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    fun updateCurrentMessage(newMessage: String) {
        currentMessage.value = newMessage
        isEnabled.value = newMessage.isNotEmpty()
    }

    fun sendMessage(message: String) {

        val aiID = "AI"
        val currentUserId = currentUserId

        val chatId = currentUserId


        // Send message to AI using http request
        val client = OkHttpClient()
        val apiKey = dotenv["API_KEY"]
        val requestBody = """
            {
                "content": "$message"
            }
        """.trimIndent()
        val request = Request.Builder()
            .url("https://chat.notadev.lat/chat")
            .header("x-api-key", apiKey)
            .post(requestBody.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("AI ERROR", "Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: "No response"

                Log.d("AI DINO", body)
                val message = Message(
                    senderId = aiID,
                    receiverId = currentUserId,
                    message = body,
                    timestamp = System.currentTimeMillis()
                )
                sendMsg(chatId, message)
                getMessages()

            }
        })

        sentMessage.value = currentMessage.value


        val message = Message(
            senderId = currentUserId,
            receiverId = aiID,
            message = currentMessage.value,
            timestamp = System.currentTimeMillis()
        )
        sendMsg(chatId, message)
        getMessages()
        currentMessage.value = ""

    }

    fun sendMsg(chatId: String, message: Message) {
        viewModelScope.launch {
            chatService.sendMessage(chatId, message, "ai_chats")
        }
    }

    fun onMenuClick(open: (String) -> Unit) {
        open(MENU_SCREEN)
    }

    fun resetMessages() {
        _messages.value = emptyList()
        chatService.resetMessages(currentUserId, "ai_chats")
        getMessages()
    }

    fun getMessages() {
        viewModelScope.launch {
            val currentUserId = currentUserId
            val chatId = currentUserId
            chatService.getMessages(chatId, "ai_chats").collect { chatMessages ->
                _messages.value = chatMessages
            }
        }
    }

}
