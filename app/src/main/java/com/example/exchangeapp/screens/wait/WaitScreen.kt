package com.example.exchangeapp.screens.wait

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource


@Composable
fun WaitScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color(0xFF0F3048))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Wait for us!", color = Color.White)

        Text("We're going as fast as possible :3", color = Color.White)
        DotLottieAnimation(
            source = DotLottieSource.Asset("cat_animation.lottie"), // from url .lottie / .json
            autoplay = true,
            loop = true,
            speed = 5f,
            useFrameInterpolation = true,
        )
    }
}