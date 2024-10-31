package com.example.exchangeapp.screens.information

import androidx.compose.runtime.mutableStateOf
import com.example.exchangeapp.DataStorage.SharedPreferencesManager
import com.example.exchangeapp.INFO_SUB_SCREEN1
import com.example.exchangeapp.INFO_SUB_SCREEN2
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ExchangeAppViewModel() {

    var clickCounter = mutableStateOf<Map<String, Int>>(emptyMap())
        private set

    init {
        Firebase.analytics.logEvent("Info_Screen",null)
        if (sharedPreferencesManager.hasStoredData()) {
            loadButtonClickInfo()
        } else {
            clickCounter.value = emptyMap() // Initialize as an empty map if no stored data
        }
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

    fun updateButtonClick(label: String) {
        val currentCounter = clickCounter.value.toMutableMap()
        currentCounter[label] = (currentCounter[label] ?: 0) + 1
        clickCounter.value = currentCounter

        sharedPreferencesManager.saveSortedButtonInfo(currentCounter.toList())
    }

    fun saveButtonClickInfo(buttonInfo: List<Pair<String, Int>>) {
        sharedPreferencesManager.saveSortedButtonInfo(buttonInfo)
    }

    private fun loadButtonClickInfo() {
        // Load saved button click data and update clickCounter state
        val savedData = sharedPreferencesManager.loadSortedButtonInfo()
        clickCounter.value = savedData.toMap()
    }

}