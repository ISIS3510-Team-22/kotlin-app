package com.example.exchangeapp.screens.sign_in

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.example.compose.ExchangeAppTheme
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(Color("#0F3048".toColorInt())),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Column (modifier = Modifier.width(280.dp)){


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
            Spacer(modifier = Modifier.padding(top = 20.dp))


            TextField(
                isError = false,
                singleLine = true,
                value = password.value,
                onValueChange = { viewModel.updatePassword(it) },
                placeholder = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { viewModel.onSignInClick (openAndPopUp) }
                )
            )

            Spacer(modifier = Modifier.padding(top = 70.dp))

            Button(
                onClick = {
                    viewModel.onSignInClick(openAndPopUp)
                },
                shape = RoundedCornerShape(35),
                colors = ButtonColors(
                    MaterialTheme.colorScheme.onTertiaryContainer,
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.secondary
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
        }
    }
}