package com.example.exchangeapp.screens.news

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.screens.TopBar


@Composable
fun NewsScreen(
    open: (String) -> Unit,
    modifier : Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel()
){

    val urls : List<String> by viewModel.webPageUrl.observeAsState(emptyList())

    Column (
        modifier.fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(22.dp)
    ) {

        TopBar(onMenuClick = { viewModel.onMenuClick(open) }, screenTitle = "NEWS",
            icon = Icons.Default.Newspaper, iconDescription = "newspaper", iconAction = {})

        LazyColumn (modifier.fillMaxSize()
            .padding(bottom = 16.dp)
            ,verticalArrangement = Arrangement.Center) {
            items(urls){item ->
                WebPageViewer(item)
                Spacer(modifier.height(16.dp))
            }
        }
    }

}

@Composable
fun WebPageViewer (url:String){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        }, modifier = Modifier.fillMaxWidth().height(400.dp))
    }
}
