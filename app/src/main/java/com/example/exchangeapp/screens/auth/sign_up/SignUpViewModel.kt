package com.example.exchangeapp.screens.auth.sign_up

import com.example.exchangeapp.NAVIGATION_SCREEN
import com.example.exchangeapp.SIGN_UP_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.example.exchangeapp.screens.auth.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService
) : ExchangeAppViewModel() {
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
    val isEnabled = MutableStateFlow(false)
    val passwordError = MutableStateFlow("")
    val confirmError = MutableStateFlow("")
    val emailError = MutableStateFlow("")

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

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
        confirmError.value = ValidationUtils.validatePasswordsMatch(password.value, newConfirmPassword)
        updateEnabled()
    }

    fun updateEnabled() {
        isEnabled.value =
            passwordError.value.isEmpty() && confirmError.value.isEmpty() && emailError.value.isEmpty()
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            if (password.value != confirmPassword.value) {
                throw Exception("Passwords do not match")
            }
            accountService.signUp(email.value, password.value)
            openAndPopUp(NAVIGATION_SCREEN, SIGN_UP_SCREEN)
        }
    }

}