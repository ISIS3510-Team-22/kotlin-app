package com.example.exchangeapp.screens.information

import com.example.exchangeapp.CHAT_SCREEN
import com.example.exchangeapp.INFO_SCREEN
import com.example.exchangeapp.INFO_SUB_SCREEN1
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.NAVIGATION_SCREEN
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor() : ExchangeAppViewModel() {

    fun onChatClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(CHAT_SCREEN, NAVIGATION_SCREEN)
        }
    }

    fun onMenuClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(MENU_SCREEN, INFO_SCREEN)
        }
    }

    fun onSubViewClick(openAndPopUp: (String, String) -> Unit){
        launchCatching {
            openAndPopUp(INFO_SUB_SCREEN1, INFO_SCREEN)
        }
    }
}