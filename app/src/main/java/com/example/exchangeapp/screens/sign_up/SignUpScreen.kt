package com.example.exchangeapp.screens.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()
    val confirmPassword = viewModel.confirmPassword.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(Color("#0F3048".toColorInt())),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Spacer(modifier = Modifier.padding(bottom = 30.dp))
        TextField(
            isError = false,
            singleLine = true,
            value = email.value,
            onValueChange = { viewModel.updateEmail(it) },
            placeholder = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )
        Spacer(modifier = Modifier.padding(top = 30.dp))


        TextField(
            isError = false,
            singleLine = true,
            value = password.value,
            onValueChange = { viewModel.updatePassword(it) },
            visualTransformation = PasswordVisualTransformation(),
            placeholder = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.padding(top = 30.dp))

        TextField(
            isError = false,
            singleLine = true,
            value = confirmPassword.value,
            onValueChange = { viewModel.updateConfirmPassword(it) },
            visualTransformation = PasswordVisualTransformation(),
            placeholder = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.padding(top = 50.dp))


        Button(
            onClick = {
                viewModel.onSignUpClick(openAndPopUp)
            },
            shape = RoundedCornerShape(20),
            colors = ButtonColors(
                MaterialTheme.colorScheme.onTertiaryContainer,
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(stringResource(R.string.sign_up), fontSize = 18.sp)

        }

    }

}