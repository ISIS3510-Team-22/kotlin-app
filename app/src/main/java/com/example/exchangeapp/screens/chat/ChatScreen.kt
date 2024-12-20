package com.example.exchangeapp.screens.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.MessageBox
import com.example.exchangeapp.screens.MessageBubble
import com.example.exchangeapp.screens.connectivityStatus
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    popUp: () -> Unit,
    receiverName: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val currentUserId = viewModel.currentUserId
    val currentMessage = viewModel.currentMessage.collectAsState()
    val isEnabled = viewModel.isEnabled.collectAsState()
    val context = LocalContext.current
    val connectionAvailable = connectivityStatus().value == ConnectionStatus.Available
    var showConnectionRestored = remember { mutableStateOf(false) }

    ToastListener(viewModel)

    LaunchedEffect(connectionAvailable) {
        if (connectionAvailable) {
            showConnectionRestored.value = true
            delay(2000)
            showConnectionRestored.value = false
        }


    }

    if (connectionAvailable){
        viewModel.getMessages(receiverName)
    }
    else {
        viewModel.getMessagesFromCache(context, receiverName)
    }



    val imeVisible = WindowInsets.isImeVisible

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .imePadding()
            .padding(start = 20.dp, end = 20.dp, bottom = if (!imeVisible) 5.dp else 0.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { popUp() },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = receiverName,
                color = Color.White,
                fontSize = 26.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.padding(end = 36.dp))

        }

        if (messages.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color(0xFF0F3048))
                    .padding(bottom = 85.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DotLottieAnimation(
                    source = DotLottieSource.Asset("plane_loading.lottie"),
                    autoplay = true,
                    loop = true,
                    speed = 3f,
                    useFrameInterpolation = false,
                    modifier = Modifier.scale(0.7f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),

                ) {
                items(messages) { message ->
                    MessageBubble(
                        message = message,
                        isSentByCurrentUser = message.senderId == currentUserId
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        MessageBox(
            currentMessage = currentMessage.value,
            updateMsgFun = { viewModel.updateCurrentMessage(it) },
            isEnabled = isEnabled.value,
            sendMsgFun = {receiverName, currentMessage ->
                viewModel.sendMessage(receiverName, currentMessage)
                viewModel.getMessages(receiverName)
            },
            receiverName = receiverName
        )
    }
}


@Composable
fun ToastListener(viewModel: ChatViewModel) {
    val context = LocalContext.current


    LaunchedEffect(viewModel.errorMessage.value) {
        viewModel.errorMessage.value.let { message ->
            if (message.isNotEmpty()) {


                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                // Clear the error message after showing the toast
                viewModel.errorMessage.value = ""

            }
        }
    }
}