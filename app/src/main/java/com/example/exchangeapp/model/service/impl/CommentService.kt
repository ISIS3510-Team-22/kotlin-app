package com.example.exchangeapp.model.service.impl

import android.util.Log
import com.example.exchangeapp.model.service.module.Comment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val commentsCollection = firestore.collection("universityComments")

    fun addComment(comment: Comment, onComplete: (Boolean) -> Unit) {
        commentsCollection.add(comment)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
    fun getCommentsByUniversity(university: String): Flow<List<Comment>> = flow {

        val comments = mutableListOf<Comment>()
        val querySnapshot = commentsCollection

            .orderBy("likes", Query.Direction.DESCENDING)
            .get()
            .await()  // Necesitas usar `await()` para esperar la respuesta asincrónicamente.

        Log.d("COMMENTS", "Se obtuvieron ${querySnapshot.documents.size} comentarios")

        for (document in querySnapshot.documents) {
            val comment = document.toObject(Comment::class.java)
            comment?.let {
                if (it.university == university) {
                    comments.add(it.copy(id = document.id))
                    Log.d("COMMENTS", it.toString())
                }
            }
        }

        emit(comments)
    }.catch {
        emit(mutableListOf())  // Si ocurre un error, emite una lista vacía.
    }


    fun updateLikes(commentId: String, newLikes: Int, onComplete: (Boolean) -> Unit) {
        commentsCollection.document(commentId)
            .update("likes", newLikes)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}