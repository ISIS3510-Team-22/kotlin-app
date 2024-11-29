package com.example.exchangeapp.model.service.module

data class Comment(
    val id: String = "",
    val username: String = "",
    val university: String = "",
    val text: String = "",
    val rating: Int = 0,
    val likes: Int = 0
)