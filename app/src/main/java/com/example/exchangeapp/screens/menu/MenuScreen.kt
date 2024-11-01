package com.example.exchangeapp.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.R

@Composable
fun MenuScreen(
    clearAndNavigate: (String) -> Unit,
    popUp: ()->Unit,
    viewModel: MenuViewModel = hiltViewModel(),
    open: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFF0F3048))
    ) {
        Row(verticalAlignment = Alignment.Top) {
            IconButton(
                onClick = {popUp()},
                modifier = Modifier
                    .size(100.dp)
                    .padding(25.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "",
                    modifier = Modifier
                        .size(60.dp),
                    tint = Color.White
                )
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 150.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { viewModel.logOut(clearAndNavigate) },
            ) {
                Text(
                    stringResource(R.string.log_out),
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontSize = 40.sp
                )
            }

            Spacer(Modifier.padding(20.dp))

            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { /*TODO()*/ },
            ) {
                Text(
                    stringResource(R.string.options),
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontSize = 40.sp
                )
            }

            Spacer(Modifier.padding(20.dp))

            TextButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { viewModel.onProfileClick(open)},
            ) {
                Text(
                    stringResource(R.string.profile),
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontSize = 40.sp
                )
            }
        }


    }
}