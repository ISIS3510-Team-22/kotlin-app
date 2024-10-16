package com.example.exchangeapp.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


open class ExchangeAppViewModel: ViewModel() {
    var errorMessage = mutableStateOf("")
    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler{_, throwable->
                errorMessage.value = throwable.message.orEmpty()
                Log.d(ERROR_TAG, throwable.message.orEmpty())
            },
            block = block
        )

    companion object{
        const val ERROR_TAG = "EXCHANGE APP ERROR"
    }
}