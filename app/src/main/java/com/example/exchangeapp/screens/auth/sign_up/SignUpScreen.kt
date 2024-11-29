package com.example.exchangeapp.screens.auth.sign_up

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.R
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.ConnectionBackBox
import com.example.exchangeapp.screens.CustomTextField
import com.example.exchangeapp.screens.EmailTextField
import com.example.exchangeapp.screens.NoInternetBox
import com.example.exchangeapp.screens.connectivityStatus
import kotlinx.coroutines.delay

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
    val errorColor = Color(0xFFE63022)

    val connectionAvailable = connectivityStatus().value == ConnectionStatus.Available


    val imeInsets = WindowInsets.ime
    val isKeyboardPresent = imeInsets.asPaddingValues().calculateBottomPadding() != 0.0.dp
    var wasConnectionAvailable = remember { mutableStateOf(true) }

    ToastListener(viewModel)


    var showConnectionRestored = remember { mutableStateOf(false) }

    LaunchedEffect(connectionAvailable) {
        if (connectionAvailable  && !wasConnectionAvailable.value) {
            showConnectionRestored.value = true
            delay(2000)
            showConnectionRestored.value = false
        }
        wasConnectionAvailable.value = connectionAvailable
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .animateContentSize()
            .background(Color(0xFF0F3048)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        NoInternetBox(connectionAvailable)
        ConnectionBackBox(showConnectionRestored.value)

        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = modifier
                .width(280.dp)
                .imePadding()
        ) {


            val size by animateDpAsState(getTargetValue(isKeyboardPresent, 100.dp, 200.dp))

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

            EmailTextField(
                email.value,
                { viewModel.updateEmail(it) },
                emailError.value,
                errorColor,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )
            CustomTextField(
                password.value,
                { viewModel.updatePassword(it) },
                ImeAction.Next,
                placeHolder = "Password",
                type = KeyboardType.Password,
                isPassword = true
            )
            if (passwordError.value != "") {
                Text(
                    passwordError.value,
                    color = errorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
            } else {
                Spacer(modifier = Modifier.padding(top = 30.dp))
            }


            val actionConfirm = getImeAction(isEnabled.value && connectionAvailable)
            CustomTextField(
                value = confirmPassword.value,
                { viewModel.updateConfirmPassword(it) },
                placeHolder = "Confirm Password",
                type = KeyboardType.Password,
                action = actionConfirm,
                onSend = { viewModel.onSignUpClick(openAndPopUp) },
                isPassword = true
            )
            if (confirmError.value != "") {
                Text(
                    confirmError.value,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = errorColor,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.padding(top = 50.dp))

            Button(
                onClick = {
                    viewModel.onSignUpClick(openAndPopUp)
                },
                enabled = isEnabled.value && connectionAvailable,
                shape = RoundedCornerShape(20),
                colors = ButtonColors(
                    Color(0xFF01397D),
                    MaterialTheme.colorScheme.onPrimary,
                    Color.Gray,
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
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold
                )

            }
        }
        Spacer(modifier = Modifier.weight(1f))

    }

}

@Composable
fun ToastListener(viewModel: SignUpViewModel) {
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

fun getImeAction(isEnabled: Boolean): ImeAction {
    return if (isEnabled) ImeAction.Send else ImeAction.Done
}

fun getTargetValue(condition: Boolean, option1: Dp, option2: Dp): Dp {
    return if (condition) option1
    else option2
}