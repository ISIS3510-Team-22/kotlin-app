package com.example.exchangeapp.screens.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor() : ExchangeAppViewModel() {
    private  val _webPageUrls = MutableLiveData<List<String>>()
    val webPageUrl : LiveData<List<String>> = _webPageUrls

    init {
        _webPageUrls.value = listOf(
            "https://www.theguardian.com/education",
            "https://www.nytimes.com/international/section/education",
            "https://www.euronews.com/tag/entertainment",
            "https://www.ft.com/eu-economy"
        )
    }

    fun onMenuClick(open: (String) -> Unit) {
        launchCatching {
            open(MENU_SCREEN)
        }
    }

    fun onWebViewClick(url:String){
        val categories = mapOf(
            "https://www.theguardian.com/education" to "Education",
            "https://www.nytimes.com/international/section/education" to "Education",
            "https://www.euronews.com/tag/entertainment" to "Entertainment",
            "https://www.ft.com/eu-economy" to "Economy"
        )
        categories[url]?.let { Firebase.analytics.logEvent(it, null) }
    }

}