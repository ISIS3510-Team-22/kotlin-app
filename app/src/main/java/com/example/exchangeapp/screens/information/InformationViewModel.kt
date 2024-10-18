package com.example.exchangeapp.screens.information

import com.example.exchangeapp.INFO_SUB_SCREEN1
import com.example.exchangeapp.INFO_SUB_SCREEN2
import com.example.exchangeapp.MENU_SCREEN
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

    fun onSubViewClick(name: String, open: (String) -> Unit) {
        val basics = listOf(
            "recipes",
            "mental_health",
            "adapting_tips"
        )
        if (name in basics)
            open("$INFO_SUB_SCREEN1/$name")
        else
            open("$INFO_SUB_SCREEN2/$name")

    }
}