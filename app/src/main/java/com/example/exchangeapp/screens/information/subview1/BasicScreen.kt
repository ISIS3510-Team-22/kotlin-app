package com.example.exchangeapp.screens.information.subview1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun BasicScreen(
    modifier : Modifier = Modifier,
    viewModel: BasicScreenViewModel = hiltViewModel()
){

    var info by remember { mutableStateOf(emptyList<String>()) }

    viewModel.getDocument { title, description ->
        info = listOf(title,description)
    }

    Column {
        Row  {
            if (info.isNotEmpty()){
                Text(text = info[0])
                Spacer(modifier.padding(8.dp))
                Text(text = info[1])
            }else {
                Text(text = "Loading ...")
            }
        }
    }

}