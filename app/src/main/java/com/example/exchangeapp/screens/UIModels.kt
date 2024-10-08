package com.example.exchangeapp.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(
    value: String,
    updateFunction: (String) -> Unit,
    action: ImeAction,
    placeHolder: String,
    type: KeyboardType,
    onSend: () -> Unit = {},
    transformation: VisualTransformation = VisualTransformation.None
) {

    TextField(
        modifier = Modifier.clip(RoundedCornerShape(25.dp)),
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
        visualTransformation = transformation,
        keyboardActions = KeyboardActions(
            onSend = { onSend() }
        )
    )
}