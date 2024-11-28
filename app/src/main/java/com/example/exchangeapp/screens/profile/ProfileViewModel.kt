package com.example.exchangeapp.screens.profile

import android.content.SharedPreferences
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.User
import com.example.exchangeapp.model.service.UserRepository
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val accountService: AccountService,
    private val sharedPreferences: SharedPreferences,
) : ExchangeAppViewModel() {

    private val gson = Gson()
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Connectivity StateFlow
    private val _isConnected = MutableStateFlow(false)

    val currentUserId = accountService.currentUserId

    init {
        loadUser()
    }

    /**
     * Set the connectivity status from the Composable.
     */
    fun setConnectivityStatus(isConnected: Boolean) {
        _isConnected.value = isConnected
        if (isConnected) {
            fetchAndCacheUser() // Fetch user if connection is available
        }
    }

    fun loadUser() {
        if (_isConnected.value) {
            fetchAndCacheUser()
        } else {
            loadFromCache()
        }
    }

    private fun fetchAndCacheUser() {
        launchCatching {
            userRepository.getCurrentUser(currentUserId).collect { user ->
                _currentUser.value = user
                cacheUser(user)
            }
        }
    }

    private fun loadFromCache() {
        val userJson = sharedPreferences.getString("cached_user", null)
        val cachedUser = userJson?.let { gson.fromJson(it, User::class.java) }
        _currentUser.value = cachedUser
    }

    private fun cacheUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString("cached_user", userJson).apply()
    }
}
