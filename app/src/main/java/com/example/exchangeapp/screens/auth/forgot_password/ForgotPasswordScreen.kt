package com.example.exchangeapp.screens.auth.forgot_password

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.R
import com.example.exchangeapp.screens.CustomTextField

@SuppressLint("NewApi")
@Composable
fun ForgotPasswordScreen(
    popUp: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val email = viewModel.email.collectAsState()
    val isEnabled = viewModel.isEnabled.collectAsState()
    val emailError = viewModel.emailError.collectAsState()
    val context = LocalContext.current
    val errorColor = Color("#e63022".toColorInt())

    BackHandler {
        popUp()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .background(Color("#0F3048".toColorInt())),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Box(
            modifier = Modifier.width(290.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.recover_text),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }


        Spacer(Modifier.padding(20.dp))

        CustomTextField(
            value = email.value,
            { viewModel.updateEmail(it) },
            placeHolder = "Email",
            type = KeyboardType.Email,
            action = if (isEnabled.value) ImeAction.Send else ImeAction.Done,
            onSend = { viewModel.onSendClick () },
        )
        if (emailError.value != "") {
            Text(
                emailError.value,
                color = errorColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
        } else {
            Spacer(modifier = Modifier.padding(top = 30.dp))
        }



        Button(
            enabled = isEnabled.value,
            onClick = {
                Toast.makeText(context, "Recover email sent!", Toast.LENGTH_SHORT).show()
                viewModel.onSendClick()
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
                stringResource(R.string.send_email_text),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }


    }
}