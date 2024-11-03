package com.example.exchangeapp.model.service.module

import com.example.exchangeapp.model.service.impl.ChatService
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val chatService: ChatService
) {

}
