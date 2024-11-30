package com.example.exchangeapp.screens.universities

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.exchangeapp.ADD_COMMENT_SCREEN
import com.example.exchangeapp.model.service.module.Comment
import com.example.exchangeapp.model.service.module.ConnectionStatus
import com.example.exchangeapp.screens.ConnectionBackBox
import com.example.exchangeapp.screens.NoInternetBox
import com.example.exchangeapp.screens.comments.CommentViewModel
import com.example.exchangeapp.screens.connectivityStatus

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UniversityScreen(
    university: String,
    open: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UniversityViewModel = hiltViewModel(),
    commentViewModel: CommentViewModel = hiltViewModel(),
    popUp: () -> Unit
) {
    val connectionAvailable = connectivityStatus().value == ConnectionStatus.Available
    val wasConnectionAvailable = remember { mutableStateOf(true) }
    val showConnectionRestored = remember { mutableStateOf(false) }
    val comments by commentViewModel.comments.collectAsState(emptyList())
    val user by commentViewModel.currentUser.collectAsState()
    val University = university

    // State to hold the fetched university details
    val universityDetails = remember { mutableStateOf<Map<String, Any>?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    val logos = mapOf(
        "Uniandes" to "https://imagenes.eltiempo.com/files/image_1200_600/files/fp/uploads/2022/11/22/637d53ede8c49.r_d.757-619.jpeg",
        "Unal" to "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy62tKYbK8zNyB_4qS6VZV1XfrkSvdDVSEh45BQRYXXcOa_lgvkDqRX-TTfcIYwTtDpkQ&usqp=CAU"
    )


    // Fetch the university details when the screen is loaded
    LaunchedEffect(university) {
        isLoading.value = true
        viewModel.getDocument(university) { data ->
            universityDetails.value = data
            isLoading.value = false
        }
    }
    LaunchedEffect(key1 = true) {
        commentViewModel.fetchUser()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F3048))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Handle no internet or restored connection
        NoInternetBox(connectionAvailable)
        ConnectionBackBox(showConnectionRestored.value)

        Spacer(Modifier.height(16.dp))

        // Back Button and Title
        Row {
            IconButton(onClick = { popUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }
            Text(
                university,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Render the Details Composable
        if (isLoading.value) {
            Text(
                "Loading...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        } else {
            universityDetails.value?.let { details ->
                Details(
                    data = details,
                    imageUrl = logos[university] ?: "https://www.google.com/url?sa=i&url=https%3A%2F%2Fcharatoon.com%2F%3Fid%3D5452&psig=AOvVaw0BWkGpwxcRFZsfmwX89AFI&ust=1733010275187000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCJC_-cfcgooDFQAAAAAdAAAAABAKhttps://www.google.com/url?sa=i&url=https%3A%2F%2Fcharatoon.com%2F%3Fid%3D5452&psig=AOvVaw0BWkGpwxcRFZsfmwX89AFI&ust=1733010275187000&source=images&cd=vfe&opi=89978449&ved=0CBQQjRxqFwoTCJC_-cfcgooDFQAAAAAdAAAAABAK"
                )
            } ?: Text(
                "No details available for $university.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }

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
                color = if (connectionAvailable) White else Color.Gray
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

@Composable
fun Details(data: Map<String, Any>,imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .background(Color(0xFF0F3048))
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "University Image",
                modifier = Modifier
                    .size(150.dp)
                    .padding(end = 20.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop // Ensures the image fits nicely in the box
            )

            // Render the university details dynamically
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Country: ${data["country"] ?: "N/A"}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "City: ${data["city"] ?: "N/A"}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "# Students: ${data["students"] ?: "N/A"}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}