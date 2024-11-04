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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.TopBar
import com.example.exchangeapp.screens.connectivityStatus
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource


@Composable
fun NewsScreen(
    open: (String) -> Unit,
    modifier : Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel()
){

    val connectionAvailable = connectivityStatus().value == ConnectionStatus.Available

    val urls : List<String> by viewModel.webPageUrl.observeAsState(emptyList())

    if (connectionAvailable) {
        Column(
            modifier.fillMaxSize()
                .background(Color(0xFF0F3048))
                .padding(22.dp)
        ) {
            TopBar(
                onMenuClick = { viewModel.onMenuClick(open) },
                screenTitle = "NEWS",
                icon = Icons.Default.Newspaper,
                iconDescription = "newspaper",
                iconAction = {}
            )

            LazyColumn(
                modifier.fillMaxSize()
                    .padding(bottom = 60.dp),
                verticalArrangement = Arrangement.Center
            ) {
                items(urls) { item ->
                    WebPageViewer(item)
                    Spacer(modifier.height(16.dp))
                }
            }
        }
    } else {
        // Show the noInternetAnimation when there is no internet connection
        NoInternetAnimation(
            open,
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel
        )
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

@Composable
fun NoInternetAnimation(
    open: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel
){
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .background(color = Color(0xFF0F3048))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 60.dp)
    ){
        TopBar(
            onMenuClick = { viewModel.onMenuClick(open) },
            screenTitle = "NEWS",
            icon = Icons.Default.Newspaper,
            iconDescription = "newspaper",
            iconAction = {}
        )
        Spacer(modifier.weight(0.8f))
        Column (modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){
            Text("There is no internet connection")
            Spacer(modifier = modifier.height(8.dp))
            DotLottieAnimation(
                source = DotLottieSource.Asset("no_inet_cat.lottie"), // from url .lottie / .json
                autoplay = true,
                loop = true,
                speed = 2f,
                useFrameInterpolation = true,
            )
        }
        Spacer(modifier.weight(1.2f))
    }
}