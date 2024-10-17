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

    fun onMenuClick(open: (String) -> Unit) {
        launchCatching {
            open(MENU_SCREEN)
        }
    }

    fun onSubViewClick(open: (String) -> Unit){
            open(INFO_SUB_SCREEN1)

    }
}