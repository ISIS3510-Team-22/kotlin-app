package com.example.exchangeapp.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color(0xFF0F3048))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DotLottieAnimation(
            source = DotLottieSource.Url("https://lottie.host/bf2158b1-53bb-42b9-931d-c75bdb9fd78c/5hpZnRlEx3.lottie"), // from url .lottie / .json

            autoplay = true,
            loop = false,
            speed = 2f,
            useFrameInterpolation = false,
        )
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        viewModel.onAppStart(openAndPopUp)
    }
}