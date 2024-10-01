package com.example.exchangeapp.screens.sign_up

import com.example.exchangeapp.INFORMATION_SCREEN
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


    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }

    fun onSignUpClick(openAndPopUp:(String, String)->Unit){
        launchCatching {
            if (password.value != confirmPassword.value){
                throw Exception("Passwords do not match")
            }
            accountService.signUp(email.value, password.value)
            openAndPopUp(INFORMATION_SCREEN, SIGN_UP_SCREEN)
        }
    }

}