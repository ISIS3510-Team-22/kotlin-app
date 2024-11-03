package com.example.exchangeapp.model.service

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val email: String  = "",
    val profilePictureUrl: String? = null,
    val lat: Double? = 0.0,
    val long: Double? = 0.0,
    var dis: Double? = 0.0

)
