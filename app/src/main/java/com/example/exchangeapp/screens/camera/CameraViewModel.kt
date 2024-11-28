package com.example.exchangeapp.screens.camera

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.User
import com.example.exchangeapp.model.service.impl.UserService
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val userService: UserService,
    private val accountService: AccountService,
) : ExchangeAppViewModel() {
    val currentUserId = accountService.currentUserId

    // Initialize currentUser as null
    var currentUser: User? = null

    init {
        // Collect the currentUser flow to fetch data asynchronously
        viewModelScope.launch {
            userService.getCurrentUser(currentUserId).collect { user ->
                currentUser = user
            }
        }
    }

    fun updateProfilePictureUrl(profilePictureUrl: String) {

        // Ensure the current user is not null before attempting to update
        currentUser.let {
            viewModelScope.launch {
                userService.updateProfilePictureUrl(currentUserId, profilePictureUrl).collect { success ->
                    if (success) {
                        Log.d("IMAGEUPLOAD", "Profile picture URL updated successfully.")
                    } else {
                        Log.e("IMAGEUPLOAD", "Failed to update profile picture URL.")
                    }
                }
            }
        }
    }

}