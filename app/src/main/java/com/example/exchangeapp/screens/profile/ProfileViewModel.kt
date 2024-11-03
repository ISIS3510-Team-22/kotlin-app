package com.example.exchangeapp.screens.profile

import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.User
import com.example.exchangeapp.model.service.UserRepository
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val accountService: AccountService

) : ExchangeAppViewModel(){

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    val currentUserId = accountService.currentUserId

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        launchCatching {
            userRepository.getCurrentUser(currentUserId).collect {
                _currentUser.value = it
            }
        }
    }





}