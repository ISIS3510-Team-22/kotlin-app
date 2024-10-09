package com.example.exchangeapp.model.service

data class User(
    val id: String = "",
    val name: String = "",
    val email: String  = "",
    val profilePictureUrl: String? = null,
    val lat: Double? = 0.0,
    val long: Double? = 0.0

)
