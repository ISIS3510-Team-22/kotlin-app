package com.example.exchangeapp.screens.aichat

import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.exchangeapp.MENU_SCREEN
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class AiChatViewModel : ViewModel() {

    val dotenv = dotenv {
        directory = "/assets"
        filename = "env" // instead of '.env', use 'env'
    }

    var currentMessage = MutableStateFlow("")
    var isEnabled = MutableStateFlow(false)
    var sentMessage = MutableStateFlow("")
    var aiResponse = MutableStateFlow("")

    fun updateCurrentMessage(newMessage: String) {
        currentMessage.value = newMessage
        isEnabled.value = newMessage.isNotEmpty()
    }

    fun sendMessage(message: String) {
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

                updateAiResponse(body.substring(1, body.length - 1))
            }
        })

        sentMessage.value = currentMessage.value
        currentMessage.value = ""
    }

    fun onMenuClick(open: (String) -> Unit) {
        open(MENU_SCREEN)
    }

    fun updateAiResponse(response: String) { // Change to accept Spanned
        aiResponse.value = response
    }

    fun resetMessages() {
        aiResponse.value = ""
        sentMessage.value = ""
    }

}
