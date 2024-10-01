package com.example.exchangeapp.screens.sign_in

import com.example.exchangeapp.INFORMATION_SCREEN
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
                openAndPopUp(INFORMATION_SCREEN, SIGN_IN_SCREEN)
            }
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit){
        openAndPopUp(SIGN_UP_SCREEN, SIGN_IN_SCREEN)
    }

}