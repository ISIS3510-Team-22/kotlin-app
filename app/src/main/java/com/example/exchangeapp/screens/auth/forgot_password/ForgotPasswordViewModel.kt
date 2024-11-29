package com.example.exchangeapp.screens.auth.forgot_password

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.example.exchangeapp.screens.auth.ValidationUtils
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor() : ExchangeAppViewModel() {

    val email = MutableStateFlow("")
    val isEnabled = MutableStateFlow(false)
    val emailError = MutableStateFlow("")
    private val _connectionStatus =  MutableStateFlow<ConnectionStatus>(ConnectionStatus.Unavailable)
    val connectionStatus = _connectionStatus

    init {
        Firebase.analytics.logEvent("Forgot_Password_Screen",null)
    }

    fun updateConnectionStatus(status: ConnectionStatus) {
        _connectionStatus.value = status
    }
    fun updateEmail(newEmail: String) {
        email.value = newEmail
        emailError.value = ValidationUtils.validateEmail(email.value)
        isEnabled.value = emailError.value.isEmpty()
    }

    fun onSendClick(context: Context) {
        if (connectionStatus.value == ConnectionStatus.Unavailable) {
            enqueuePasswordResetEmail(email.value, context)
        } else {
            Firebase.auth.sendPasswordResetEmail(email.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                    } else {
                        Log.e(TAG, "Failed to send email: ${task.exception}")
                    }
                }
        }
    }


    fun enqueuePasswordResetEmail(email: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("PendingEmails", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("password_reset_email", email)
        editor.apply()
    }

    fun processPendingEmailRequests(context: Context) {
        val sharedPreferences = context.getSharedPreferences("PendingEmails", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("password_reset_email", null)

        email?.let {
            Firebase.auth.sendPasswordResetEmail(it)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("EMAIL", "Pending email sent.")
                        // Clear the pending email
                        sharedPreferences.edit().remove("password_reset_email").apply()
                    } else {
                        Log.e("EMAIL", "Failed to send email: ${task.exception}")
                    }
                }
        }
    }



}
