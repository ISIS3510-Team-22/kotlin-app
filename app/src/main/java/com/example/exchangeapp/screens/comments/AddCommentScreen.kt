package com.example.exchangeapp.screens.comments

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.exchangeapp.model.service.module.Comment

@Composable
fun AddCommentScreen(
    universityName: String,
    onBack: () -> Unit,
    viewModel: CommentViewModel = hiltViewModel(),
    popUp: () -> Unit
) {
    val commentText = remember { mutableStateOf("") }
    val rating = remember { mutableStateOf(0) }
    val context = LocalContext.current


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(){
            IconButton(onClick = { popUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(60.dp)
                )
            }
            Text("Add a Comment for $universityName", style = MaterialTheme.typography.headlineSmall)
        }


        val commentText = remember { mutableStateOf("") }

        TextField(
            value = commentText.value,
            onValueChange = { commentText.value = it },
            label = { Text("Your Comment") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        StarRating(rating = rating.value) { newRating -> rating.value = newRating }

        Button(
            onClick = {
                val comment = Comment(

                    university = universityName,
                    text = commentText.value,
                    rating = rating.value,
                    likes = 0
                )
                viewModel.addComment(comment) { success ->
                    if (success) {
                        onBack() // Volver atrás si el comentario fue exitoso
                    } else {
                        // Mostrar un mensaje de error si hubo un fallo
                        Toast.makeText(context, "Failed to add comment", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
            Text("Submit")
        }
    }
}

@Composable
fun StarRating(rating: Int, onRatingSelected: (Int) -> Unit) {
    Row {
        (1..5).forEach { star ->
            IconButton(onClick = { onRatingSelected(star) }) {
                Icon(
                    imageVector = if (star <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = null
                )
            }
        }
    }
}
