package com.example.exchangeapp.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


open class ExchangeAppViewModel: ViewModel() {
    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler{_, throwable->
                Log.d(ERROR_TAG, throwable.message.orEmpty())
            },
            block = block
        )

    companion object{
        const val ERROR_TAG = "EXCHANGE APP ERROR"
    }
}