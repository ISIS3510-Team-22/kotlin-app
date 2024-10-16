package com.example.exchangeapp.model.service

import com.example.exchangeapp.model.service.impl.UserService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    fun getUserNames(): Flow<List<String>> {
        return userService.getUserNames()
    }
}