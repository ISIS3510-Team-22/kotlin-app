package com.example.exchangeapp.screens.Information

import com.example.exchangeapp.CHAT_SCREEN
import com.example.exchangeapp.NAVIGATION_SCREEN
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(): ExchangeAppViewModel() {

    fun onChatClick(openAndPopUp : (String,String) -> Unit){
        launchCatching {
            openAndPopUp(CHAT_SCREEN, NAVIGATION_SCREEN)
        }
    }
}