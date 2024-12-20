package com.example.exchangeapp.model.service.module

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id : String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)