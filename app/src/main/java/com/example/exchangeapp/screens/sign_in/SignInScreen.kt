package com.example.exchangeapp.screens.sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.ExchangeAppTheme
import com.example.compose.backgroundDark
import com.example.exchangeapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    openAndPopUp: (String, String) -> Unit,
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
        Image(
            painter = (painterResource(R.drawable.asset_1)), contentDescription = "",
            Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.padding(bottom = 40.dp))
        TextField(
            isError = true,
            singleLine = true,
            value = email.value,
            onValueChange = { viewModel.updateEmail(it) })
        Spacer(modifier = Modifier.padding(top = 20.dp))


        TextField(
            isError = true,
            singleLine = true,
            value = password.value,
            onValueChange = { viewModel.updatePassword(it) },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.padding(top = 40.dp))


        Button(
            onClick = {
                viewModel.onSignInClick(openAndPopUp)
            },
            shape = RoundedCornerShape(20),
            colors = ButtonColors(
                MaterialTheme.colorScheme.onTertiaryContainer,
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.secondary
            )
        ) {
            Text(stringResource(R.string.sign_in_text), fontSize = 18.sp)

        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable()
fun SignInPreview() {
    ExchangeAppTheme {
        SignInScreen({ _, _ -> })
    }
}

