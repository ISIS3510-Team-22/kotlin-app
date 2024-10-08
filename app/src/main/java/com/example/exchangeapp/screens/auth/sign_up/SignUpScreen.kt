package com.example.exchangeapp.screens.auth.sign_up

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.R
import com.example.exchangeapp.screens.CustomTextField

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    popUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    BackHandler {
        popUp()
    }

    val email = viewModel.email.collectAsState()
    val name = viewModel.name.collectAsState()
    val password = viewModel.password.collectAsState()
    val confirmPassword = viewModel.confirmPassword.collectAsState()
    val isEnabled = viewModel.isEnabled.collectAsState()
    val passwordError = viewModel.passwordError.collectAsState()
    val confirmError = viewModel.confirmError.collectAsState()
    val emailError = viewModel.emailError.collectAsState()


    val imeInsets = WindowInsets.ime
    val isKeyboardPresent = imeInsets.asPaddingValues().calculateBottomPadding() != 0.0.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .animateContentSize()
            .background(Color("#0F3048".toColorInt())),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Column(
            modifier = modifier.width(280.dp)
        ) {


            val size by animateDpAsState(targetValue = if (isKeyboardPresent) 100.dp else 200.dp)
            val pad by animateDpAsState(targetValue = if (isKeyboardPresent) 40.dp else 0.dp)

            Spacer(modifier = Modifier.padding(top = pad))
            Image(
                painter = painterResource(R.drawable.sign_up_logo),
                contentDescription = stringResource(R.string.sign_up_image),
                modifier = Modifier
                    .size(size)
                    .align(Alignment.CenterHorizontally)
                    .animateContentSize()
            )

            CustomTextField(
                name.value, { viewModel.updateName(it) },
                ImeAction.Next,
                "Name",
                KeyboardType.Text
            )

            Spacer(modifier = Modifier.padding(bottom = 30.dp))

            CustomTextField(
                email.value,
                { viewModel.updateEmail(it) },
                ImeAction.Next, "Email",
                type = KeyboardType.Email
            )
            if (emailError.value != "") {
                Text(
                    emailError.value,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
            } else {
                Spacer(modifier = Modifier.padding(top = 30.dp))
            }


            CustomTextField(
                password.value,
                { viewModel.updatePassword(it) },
                ImeAction.Next,
                placeHolder = "Password",
                type = KeyboardType.Password,
                transformation = PasswordVisualTransformation()
            )
            if (passwordError.value != "") {
                Text(
                    passwordError.value,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
            } else {
                Spacer(modifier = Modifier.padding(top = 30.dp))
            }


            CustomTextField(
                value = confirmPassword.value,
                { viewModel.updateConfirmPassword(it) },
                placeHolder = "Confirm Password",
                type = KeyboardType.Password,
                action = if (isEnabled.value) ImeAction.Send else ImeAction.Done,
                onSend = { viewModel.onSignUpClick(openAndPopUp) },
                transformation = PasswordVisualTransformation()
            )
            if (confirmError.value != "") {
                Text(confirmError.value,modifier=Modifier.align(Alignment.CenterHorizontally), color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Button(
                onClick = {
                    viewModel.onSignUpClick(openAndPopUp)
                },
                enabled = isEnabled.value,
                shape = RoundedCornerShape(20),
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
                    stringResource(R.string.sign_up),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }
        }

    }

}
