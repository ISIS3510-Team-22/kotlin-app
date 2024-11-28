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

    val categories = mapOf(
        "https://www.theguardian.com/education" to "Education",
        "https://www.nytimes.com/international/section/education" to "Education",
        "https://apnews.com/education" to "Education",
        "https://www.usnews.com/topics/subjects/education" to "Education",
        "https://www.euronews.com/tag/entertainment" to "Entertainment",
        "https://www.etonline.com/" to "Entertainment",
        "https://edition.cnn.com/entertainment" to "Entertainment",
        "https://abcnews.go.com/Entertainment" to "Entertainment",
        "https://www.ft.com/eu-economy" to "Economy",
        "https://www.bbc.com/news/business/economy" to "Economy",
        "https://www.nytimes.com/section/business/economy" to "Economy",
        "https://edition.cnn.com/business/economy" to "Economy"
    )

    init {
        // Select 4 random keys (URLs) from the categories map
        _webPageUrls.value = categories.keys.shuffled().take(4).toList()
    }

    fun onMenuClick(open: (String) -> Unit) {
        launchCatching {
            open(MENU_SCREEN)
        }
    }

    fun onWebViewClick(url:String){
        categories[url]?.let { Firebase.analytics.logEvent(it, null) }

        // Update the URL list based on the category of the clicked URL
        val clickedCategory = categories[url]
        if (clickedCategory != null) {
            _webPageUrls.value = categories.filterValues { it == clickedCategory }.keys.toList()
        }
    }

}