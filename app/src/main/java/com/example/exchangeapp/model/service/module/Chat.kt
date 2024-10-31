package com.example.exchangeapp.model.service.module

import kotlinx.serialization.Serializable

@Serializable
data class Chat (
    val id : String = "",
    val users: List<String>,
    val messages: List<Message>
)