package com.example.exchangeapp.screens.auth.sign_in

import com.example.exchangeapp.NAVIGATION_SCREEN
import com.example.exchangeapp.SIGN_IN_SCREEN
import com.example.exchangeapp.SIGN_UP_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.example.exchangeapp.screens.auth.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService
) : ExchangeAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val emailError = MutableStateFlow("")
    val passwordError = MutableStateFlow("")
    val isEnabled = MutableStateFlow(false)

    fun updateEmail(newEmail: String) {
        email.value = newEmail
        emailError.value = ValidationUtils.validateEmail(newEmail)
        updateEnabled()
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
        passwordError.value = ValidationUtils.validatePassword(newPassword)
        updateEnabled()

    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.signIn(email.value, password.value)
            openAndPopUp(NAVIGATION_SCREEN, SIGN_IN_SCREEN)
        }
    }

    fun onSignUpClick(open: (String) -> Unit) {
        open(SIGN_UP_SCREEN)
    }

    fun updateEnabled() {
        isEnabled.value = passwordError.value.isEmpty() && emailError.value.isEmpty()
    }

}