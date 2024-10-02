package com.example.exchangeapp.screens.sign_in

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import com.example.exchangeapp.NAVIGATION_SCREEN
import com.example.exchangeapp.R
import com.example.exchangeapp.SIGN_IN_SCREEN
import com.example.exchangeapp.SIGN_UP_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService
) : ExchangeAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
                accountService.signIn(email.value, password.value)
                openAndPopUp(NAVIGATION_SCREEN, SIGN_IN_SCREEN)
            }
    }

    fun onSingUpClick(openAndPopUp: (String, String) -> Unit){
        openAndPopUp(SIGN_UP_SCREEN, SIGN_IN_SCREEN)
    }

}