package com.example.exchangeapp.screens.information

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.exchangeapp.INFO_SUB_SCREEN1
import com.example.exchangeapp.INFO_SUB_SCREEN2
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor() : ExchangeAppViewModel() {

    init {
        Firebase.analytics.logEvent("Info_Screen",null)
    }

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

    var clickCounter by mutableStateOf(mutableMapOf<String, Int>())
        private set

    fun updateButtonClick(label: String) {
        clickCounter = clickCounter.toMutableMap().apply {
            this[label] = this.getOrDefault(label, 0) + 1
        }
    }
}