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
    val name = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail.filter { !it.isWhitespace() }
        emailError.value = ValidationUtils.validateEmail(newEmail)
        updateEnabled()
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword.filter { !it.isWhitespace() }
        passwordError.value = ValidationUtils.validatePassword(newPassword)
        if (confirmPassword.value.isNotEmpty()) {
            confirmError.value =
                ValidationUtils.validatePasswordsMatch(newPassword, confirmPassword.value)
        }
        updateEnabled()
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword.filter { !it.isWhitespace() }
        confirmError.value =
            ValidationUtils.validatePasswordsMatch(password.value, newConfirmPassword)
        updateEnabled()
    }

    fun updateName(newName: String) {
        name.value = newName
    }

    fun updateEnabled() {
        isEnabled.value =
            passwordError.value.isEmpty() && confirmError.value.isEmpty() &&
                    emailError.value.isEmpty() && password.value.isNotEmpty() &&
                    email.value.isNotEmpty() && confirmPassword.value.isNotEmpty() &&
                    name.value.isNotEmpty()
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