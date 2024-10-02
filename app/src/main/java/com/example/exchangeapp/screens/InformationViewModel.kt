package com.example.exchangeapp.screens

import com.example.exchangeapp.CHAT_SCREEN
import com.example.exchangeapp.INFORMATION_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(): ExchangeAppViewModel() {

    fun onChatClick(openAndPopUp : (String,String) -> Unit){
        launchCatching {
            openAndPopUp(CHAT_SCREEN, INFORMATION_SCREEN)
        }
    }
}