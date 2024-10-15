package com.example.exchangeapp.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
                    Icon(Icons.Filled.Visibility, "")
                }
            }
            else if (isPassword && showField.value) {
                IconButton(onClick = { showField.value = !showField.value }) {
                    Icon(Icons.Filled.VisibilityOff, "")
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
