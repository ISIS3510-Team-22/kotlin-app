package com.example.exchangeapp.screens.chat

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.impl.ChatService
import com.example.exchangeapp.model.service.module.Chat
import com.example.exchangeapp.model.service.module.Message
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import kotlin.text.filter

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatService: ChatService,
    private val accountService: AccountService
) : ExchangeAppViewModel() {

    val currentUserId = accountService.currentUserId
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()
    var currentMessage = MutableStateFlow("")
    var isEnabled = MutableStateFlow(false)
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()



    // Enviar un mensaje
    fun sendMessage(receiverName: String, messageText: String) {

        viewModelScope.launch {


            val receiverId = chatService.getUserIdByName(receiverName)
            val currentUserId = currentUserId


            if (receiverId != null) {
                val chatId =
                    if (currentUserId < receiverId) "$currentUserId-$receiverId" else "$receiverId-$currentUserId"

                Log.d("CHAT", chatId)

                val message = Message(
                    senderId = currentUserId,
                    receiverId = receiverId,
                    message = messageText,
                    timestamp = System.currentTimeMillis()
                )
                Log.d("TREX", currentUserId)
                Log.d("TREX", receiverId)
                chatService.sendMessage(chatId, message, "chats")
                updateCurrentMessage("")
            }
        }
    }

    fun getMessages(name: String) {
        viewModelScope.launch {
            val receiverId = chatService.getUserIdByName(name)
            val currentUserId = currentUserId
            val chatId =
                if (currentUserId < receiverId.toString()) "$currentUserId-$receiverId" else "$receiverId-$currentUserId"
            chatService.getMessages(chatId, "chats").collect { chatMessages ->
                _messages.value = chatMessages

                //Recupero los obj mensajes totalmente
            }
        }
    }

    fun getMessagesFromCache(context: Context, name: String) {
        viewModelScope.launch {
            errorMessage.value = "There is no internet connection"
            val receiverId = chatService.getUserIdByName(name)
            val currentUserId = currentUserId
            val chatId =
                if (currentUserId < receiverId.toString()) "$currentUserId-$receiverId" else "$receiverId-$currentUserId"

            val cachedChatsDeferred = async(Dispatchers.IO) { fetchChatsFromCache(context) }
            val cachedChats = cachedChatsDeferred.await()
            _chats.value = cachedChats

            val chat = cachedChats.find { it.id == chatId }
            if (chat != null) {
                _messages.value = chat.messages.reversed()
                Log.d("TREX", chat.messages.toString())
            }
            else {
                errorMessage.value = "Cannot start a new chat without internet connection"
            }
        }
    }

    private fun getChatSnapshotFromCache(context: Context): List<Chat>? {
        return try {
            // Leer el contenido del archivo JSON
            val cacheDir = context.cacheDir
            val file = File(cacheDir, "chat_snapshot.json")

            // Verificar si el archivo existe
            if (file.exists()) {
                val jsonString = file.readText()
                // Deserializar el JSON a una lista de objetos User
                Json.decodeFromString<List<Chat>>(jsonString)
            } else {
                Log.d("TREX", "No se encontró el archivo de caché")
                null
            }
        } catch (e: Exception) {
            // Manejar cualquier excepción, como errores de lectura o deserialización
            Log.e("TREX", "Error al leer el caché: ${e.message}")
            null
        }
    }

    private fun fetchChatsFromCache(context: Context): List<Chat> {
        return getChatSnapshotFromCache(context).orEmpty()
    }

    fun updateCurrentMessage(newMessage: String) {
        currentMessage.value = newMessage
        isEnabled.value = currentMessage.value.filter { !it.isWhitespace() }.isNotEmpty()
    }

}