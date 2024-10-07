package com.example.exchangeapp.screens.auth.sign_in

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.R

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    openAndPopUp: (String, String) -> Unit,
    open: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val email = viewModel.email.collectAsState()
    val password = viewModel.password.collectAsState()
    val emailError = viewModel.emailError.collectAsState()
    val passwordError = viewModel.passwordError.collectAsState()
    val isEnabled = viewModel.isEnabled.collectAsState()
    val errorColor = Color("#e63022".toColorInt())
    val context = LocalContext.current


    LaunchedEffect(viewModel.errorMessage.value) {
        viewModel.errorMessage.value.let { message ->
            if (message.isNotEmpty()) {
                val showMsg =
                    if (message == "The supplied auth credential is incorrect, malformed or has expired.") {
                        "Incorrect credentials, please try again"
                    } else {
                        message
                    }

                Toast.makeText(context, showMsg, Toast.LENGTH_SHORT).show()
                // Clear the error message after showing the toast
                viewModel.errorMessage.value = ""

            }
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .background(Color("#0F3048".toColorInt())),
        Arrangement.Center,
        Alignment.CenterHorizontally

    ) {
        Column(modifier = Modifier.width(280.dp)) {


            Image(
                painter = (painterResource(R.drawable.asset_1)), contentDescription = "",
                modifier = Modifier
                    .size(128.dp)
                    .align(Alignment.CenterHorizontally)

            )

            Spacer(modifier = Modifier.padding(bottom = 40.dp))
            TextField(
                isError = false,
                singleLine = true,
                value = email.value,
                onValueChange = { viewModel.updateEmail(it) },
                placeholder = { Text("Email") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
            if (emailError.value != "") {
                Text(
                    emailError.value,
                    color = errorColor,
                    modifier = Modifier.width(280.dp)
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
            } else {
                Spacer(modifier = Modifier.padding(top = 30.dp))
            }


            TextField(
                isError = false,
                singleLine = true,
                value = password.value,
                onValueChange = { viewModel.updatePassword(it) },
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (isEnabled.value) ImeAction.Send else ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onSend = { viewModel.onSignInClick(openAndPopUp) }
                )
            )

            if (passwordError.value != "") {
                Text(
                    passwordError.value,
                    color = errorColor,
                    modifier = Modifier.width(280.dp)
                )
                Spacer(modifier = Modifier.padding(top = 60.dp))
            } else {
                Spacer(modifier = Modifier.padding(top = 70.dp))
            }

            Button(
                enabled = isEnabled.value,
                onClick = {
//                    Toast.makeText(context, "Logging in", Toast.LENGTH_SHORT).show()
                    viewModel.onSignInClick(openAndPopUp)
                },
                shape = RoundedCornerShape(35),
                colors = ButtonColors(
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.tertiary,
                    MaterialTheme.colorScheme.onTertiary
                ),
                modifier = Modifier
                    .height(55.dp)
                    .width(130.dp)
                    .shadow(elevation = 5.dp, RoundedCornerShape(35))
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    stringResource(R.string.sign_in_text),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

            }
            Spacer(modifier = Modifier.padding(top = 10.dp))

            TextButton(
                onClick = { viewModel.onSignUpClick(open) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    textDecoration = TextDecoration.Underline,
                    color = Color.White,
                    text = stringResource(R.string.sign_up_text)
                )
            }

            TextButton(
                onClick = { viewModel.onForgotClick(open) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    textDecoration = TextDecoration.Underline,
                    color = Color.White,
                    text = stringResource(R.string.forgot_password)
                )
            }
        }

    }


}