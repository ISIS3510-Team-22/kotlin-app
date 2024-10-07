package com.example.exchangeapp.screens.auth.forgot_password

import android.content.ContentValues.TAG
import android.util.Log
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.example.exchangeapp.screens.auth.ValidationUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor() : ExchangeAppViewModel() {

    val email = MutableStateFlow("")
    val isEnabled = MutableStateFlow(false)
    val emailError = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
        emailError.value = ValidationUtils.validateEmail(email.value)
        isEnabled.value = emailError.value.isEmpty()
    }

    fun onSendClick(){
        Firebase.auth.sendPasswordResetEmail(email.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }

    }

}
