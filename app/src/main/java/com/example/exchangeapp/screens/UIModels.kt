package com.example.exchangeapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.model.service.module.Message
import com.example.exchangeapp.model.service.module.currentConnectivityStatus
import com.example.exchangeapp.model.service.module.observeConnectivityAsFlow


@Composable
fun CustomTextField(
    value: String,
    updateFunction: (String) -> Unit,
    action: ImeAction,
    placeHolder: String,
    type: KeyboardType,
    onSend: () -> Unit = {},
    isPassword: Boolean = false
) {

    var showField = remember { mutableStateOf(!isPassword) }

    TextField(
        modifier = Modifier.clip(RoundedCornerShape(25.dp)),
        trailingIcon = {
            if (isPassword && !showField.value) {
                IconButton(onClick = { showField.value = !showField.value }) {
                    Icon(Icons.Filled.VisibilityOff, "")
                }
            } else if (isPassword && showField.value) {
                IconButton(onClick = { showField.value = !showField.value }) {
                    Icon(Icons.Filled.Visibility, "", tint = Color(0xFF0F3048))
                }
            }

        },
        singleLine = true,
        value = value,
        onValueChange = {
            updateFunction(it)
        },
        placeholder = { Text(placeHolder) },
        keyboardOptions = KeyboardOptions(
            imeAction = action,
            keyboardType = type
        ),
        visualTransformation = if (showField.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardActions = KeyboardActions(
            onSend = { onSend() }
        )
    )
}

@Composable
fun EmailTextField(
    email: String,
    updateFunction: (String) -> Unit,
    emailError: String,
    errorColor: Color,
    modifier: Modifier = Modifier
) {
    CustomTextField(
        email,
        { updateFunction(it) },
        ImeAction.Next, "Email",
        type = KeyboardType.Email,
    )
    if (emailError != "") {
        Text(
            emailError,
            color = errorColor,
            fontSize = 12.sp,
            modifier = modifier
        )
        Spacer(modifier = modifier.padding(top = 10.dp))
    } else {
        Spacer(modifier = modifier.padding(top = 30.dp))
    }
}


@Composable
fun TopBar(
    onMenuClick: () -> Unit, screenTitle: String, icon: ImageVector, iconDescription: String,
    iconAction: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "",
                modifier = Modifier.size(60.dp),
                tint = Color.White
            )
        }
        Text(
            text = screenTitle,
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = iconAction,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }

}

@Composable
fun connectivityStatus(): State<ConnectionStatus> {
    val mCtx = LocalContext.current

    return produceState(initialValue = mCtx.currentConnectivityStatus) {
        mCtx.observeConnectivityAsFlow().collect { value = it }
    }

}

@Composable
fun NoInternetBox(connectionAvailable: Boolean) {
    AnimatedVisibility(
        visible = !connectionAvailable,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFF44336)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "There is no internet connection",
                color = Color.White
            )
        }
    }
}

@Composable
fun ConnectionBackBox(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color(0xFF4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Text("Connection restored", color = Color.White)
        }
    }
}

@Composable
fun MessageBox(
    currentMessage: String,
    updateMsgFun: (String) -> Unit,
    sendMsgFun: (String, String) -> Unit = { _, _ -> },
    sendMsgFunAI: (String) -> Unit = {},
    isEnabled: Boolean,
    isAiChat: Boolean = false,
    receiverName: String = "",

) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = currentMessage,
            onValueChange = { updateMsgFun(it) },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            shape = MaterialTheme.shapes.large,
            placeholder = { Text("Write a message...", color = Color(0xFFE8E8E8)) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Gray,
                selectionColors = TextSelectionColors(
                    handleColor = Color.White,
                    backgroundColor = Color.LightGray
                )
            )
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFFFFFFF))
        ) {
            IconButton(
                onClick =if (isAiChat) {
                    { sendMsgFunAI(currentMessage) }
                }else{
                    { sendMsgFun(receiverName, currentMessage) }
                },
                enabled = isEnabled
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    "Send button",
                    tint = if (isEnabled) Color(0xFF0F3048) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isSentByCurrentUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .wrapContentWidth(if (isSentByCurrentUser) Alignment.End else Alignment.Start)
    ) {
        Text(
            text = message.message,
            style = MaterialTheme.typography.bodyLarge.copy(),
            color = if (isSentByCurrentUser) Color.White else Color.Black,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .background(
                    color = if (isSentByCurrentUser) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(10.dp)
        )
    }
}
