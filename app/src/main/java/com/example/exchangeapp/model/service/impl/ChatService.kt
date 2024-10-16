package com.example.exchangeapp.model.service.impl

import android.util.Log
import com.example.exchangeapp.CHAT_SCREEN
import com.example.exchangeapp.model.service.module.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth // Agregar FirebaseAuth para manejar el usuario autenticado
) {

    val accountServiceImpl = AccountServiceImpl()

    // Obtiene el userId del usuario autenticado

    fun getCurrentUserId(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }

    // Obtiene el userId del destinatario a partir de su nombre
    suspend fun getUserIdByName(userName: String): String? {
        val querySnapshot = firestore.collection("users")
            .whereEqualTo("name", userName)
            .get().await()

        return if (querySnapshot.documents.isNotEmpty()) {
            querySnapshot.documents[0].getString("id")
        } else {
            null
        }
    }

    // Obtener los mensajes del chat
    fun getMessages(chatId: String): Flow<List<Message>> = flow {
        val messages = mutableListOf<Message>()
        val querySnapshot = firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .get().await()

        for (document in querySnapshot.documents) {
            val message = document.toObject(Message::class.java)
            message?.let { messages.add(it) }
        }

        emit(messages)
    }.catch {
        emit(mutableListOf())
    }

    // Enviar un mensaje
    suspend fun sendMessage(chatId: String, message: Message) {
        Log.d("DINOSAURIO", message.message)
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
            .await()
    }

    fun getChatId(user2: String): String {
        val user1 = getCurrentUserId()
        val chatId = if (user1!! < user2) "$user1-$user2" else "$user2-$user1"
        return chatId
    }

    fun createChat(user2: String, open: (String) -> Unit, userName: String, currentUserId: String) {
        val user1 = currentUserId
        val chatId = if (user1 < user2) "$user1-$user2" else "$user2-$user1"
        val chatRef = firestore.collection("chats").document(chatId)

        chatRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                // Crear un nuevo chat
                chatRef.set(mapOf("users" to listOf(user1, user2)))
                    .addOnSuccessListener {
                        // Chat creado exitosamente, pasa el chatId
                        open("$CHAT_SCREEN/$userName")
                    }
                    .addOnFailureListener {
                        // Manejar el error si ocurre
                    }
            } else {
                // Si el chat ya existe, pasa el chatId directamente
                open("$CHAT_SCREEN/$userName")
            }
        }.addOnFailureListener {
            // Manejar error si la consulta falla
        }
    }

}
