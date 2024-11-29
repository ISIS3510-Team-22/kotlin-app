package com.example.exchangeapp.screens.universities

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.ADD_COMMENT_SCREEN
import com.example.exchangeapp.model.service.module.Comment
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.ConnectionBackBox
import com.example.exchangeapp.screens.NoInternetBox
import com.example.exchangeapp.screens.comments.CommentViewModel
import com.example.exchangeapp.screens.connectivityStatus
import kotlinx.coroutines.delay

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UniversityScreen(
    university : String,
    open: (String) -> Unit,
    modifier : Modifier = Modifier,
    viewModel: UniversityViewModel = hiltViewModel(),
    commentViewModel: CommentViewModel = hiltViewModel(),
    popUp: () -> Unit
){
    val connectionAvailable = connectivityStatus().value == ConnectionStatus.Available
    var wasConnectionAvailable = remember { mutableStateOf(true) }
    var showConnectionRestored = remember { mutableStateOf(false) }
    val comments by commentViewModel.comments.collectAsState(emptyList())
    val user by commentViewModel.currentUser.collectAsState()
    val University = university

    LaunchedEffect(university) {
        commentViewModel.fetchComments(university)
        Log.d("COMMENTS", "3" + comments.toString())
    }



    LaunchedEffect(connectionAvailable) {
        if (connectionAvailable) {
            if (connectionAvailable && !wasConnectionAvailable.value) {
                showConnectionRestored.value = true
                delay(2000)
                showConnectionRestored.value = false
            }
            wasConnectionAvailable.value = connectionAvailable
        }
    }

    LaunchedEffect(key1 = true) {
        commentViewModel.fetchUser()
    }


    Column (
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        NoInternetBox(connectionAvailable)
        ConnectionBackBox(showConnectionRestored.value)
        Spacer(Modifier.height(1.dp))
        Row {
            IconButton(
                onClick = { popUp() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    "",
                    modifier = Modifier.size(60.dp),
                    tint = White
                )
            }
            Text(university, style = MaterialTheme.typography.headlineMedium, color = White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Aca va la info de la universidad (country, city, students)")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { open("$ADD_COMMENT_SCREEN/$university") },
            enabled = connectionAvailable, // Disable button if no internet
            modifier = Modifier
                .fillMaxWidth()  // Makes the button take the full width
                .padding(vertical = 16.dp),  // Adds vertical padding to the button
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,  // Color de fondo del botón
                contentColor = White
            )
            ) {
            Text(
                "Add comments",
                style = MaterialTheme.typography.headlineMedium,
                color =  if (connectionAvailable) White else Color.Gray
            )
        }

        Text("Comments:", style = MaterialTheme.typography.headlineSmall, color = White)
        Spacer(modifier = Modifier.height(8.dp))

        if (comments.isNotEmpty()) {
            comments.forEach { comment ->
                CommentItem(
                    comment = comment,
                    onLike = { commentViewModel.likeComment(comment, comment.likes) },
                    university = university,
                    connectionAvailable = connectionAvailable
                    )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text(
                "No hay comentarios aún. ¡Sé el primero en comentar!",
                color = White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
    commentViewModel: CommentViewModel = hiltViewModel(),
    university : String,
    connectionAvailable: Boolean
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = comment.text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Puntuación: ${comment.rating} estrellas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )


        }
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = "${comment.likes} ❤️",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                if (connectionAvailable) { // Only allow "like" if there's an internet connection
                    onLike()
                    commentViewModel.fetchComments(university)
                }
            },
                enabled = connectionAvailable // Disable "like" button if no internet
            ) {
                Text("Like")
            }
        }
    }
}
