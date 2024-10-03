package com.example.exchangeapp.screens.sign_up

import com.example.exchangeapp.NAVIGATION_SCREEN
import com.example.exchangeapp.SIGN_UP_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.screens.ExchangeAppViewModel
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
        val regex = """^[\w-.]+@([\w-]+\.)+[\w-]{2,4}${'$'}""".toRegex()
        emailError.value =
            if (!regex.containsMatchIn(email.value)) {
                "Invalid email"
            } else {
                ""
            }
        updateEnabled()

    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
        val regex =
            """^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@${'$'}%^&*-]).{6,}${'$'}""".toRegex()
        passwordError.value =
            if (!regex.containsMatchIn(password.value)) {
                "Min 6 characters and special"
            } else {
                ""
            }
        updateEnabled()

    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
        confirmError.value =
            if (confirmPassword.value != password.value) {
                "Passwords do not match"
            } else {
                ""
            }
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