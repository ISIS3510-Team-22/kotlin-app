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
    val emailError = MutableStateFlow("")
    val passwordError = MutableStateFlow("")
    val isEnabled = MutableStateFlow(false)

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