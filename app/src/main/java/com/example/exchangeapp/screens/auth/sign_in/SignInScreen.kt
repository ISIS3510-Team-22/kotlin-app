package com.example.exchangeapp.screens.auth.sign_in

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.exchangeapp.screens.CustomTextField
import com.example.exchangeapp.screens.EmailTextField

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


    ToastListener(viewModel)

    val imeInsets = WindowInsets.ime
    val isKeyboardPresent = imeInsets.asPaddingValues().calculateBottomPadding() != 0.0.dp

    Column(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
            .background(Color("#0F3048".toColorInt())),
        Arrangement.Center,
        Alignment.CenterHorizontally

    ) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .imePadding()
        ) {

            val size by animateDpAsState(
                targetValue = if (isKeyboardPresent) 80.dp else 150.dp,
                label = ""
            )
            val pad by animateDpAsState(
                targetValue = if (isKeyboardPresent) 40.dp else 0.dp,
                label = ""
            )

            Spacer(modifier = Modifier.padding(top = pad))

            Image(
                painter = painterResource(R.drawable.asset_1),
                contentDescription = "",
                modifier = Modifier
                    .size(size)
                    .align(Alignment.CenterHorizontally)
            )


            Spacer(modifier = Modifier.padding(bottom = 40.dp))

            EmailTextField(
                email.value,
                { viewModel.updateEmail(it) },
                emailError.value,
                errorColor,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )


            val actionPassword = getImeAction(isEnabled.value)

            CustomTextField(
                value = password.value,
                { viewModel.updatePassword(it) },
                placeHolder = "Password",
                type = KeyboardType.Password,
                action = actionPassword,
                onSend = { viewModel.onSignInClick(openAndPopUp) },
                transformation = PasswordVisualTransformation()
            )

            if (passwordError.value != "") {
                Text(
                    passwordError.value,
                    color = errorColor,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.padding(top = 10.dp))
            } else {
                Spacer(modifier = Modifier.padding(top = 30.dp))
            }


            Button(
                enabled = isEnabled.value,
                onClick = {
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

@Composable
fun ToastListener(viewModel: SignInViewModel) {
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
}


fun getImeAction(isEnabled: Boolean): ImeAction {
    return if (isEnabled) ImeAction.Send else ImeAction.Done
}