package com.example.exchangeapp.model.service.impl


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getUserNames(): Flow<List<String>> = flow {
        val userList = mutableListOf<String>()
        val querySnapshot = firestore.collection("users").get().await()
        for (document in querySnapshot.documents) {
            val name = document.getString("name")
            name?.let { userList.add(it) }
        }
        emit(userList)
    }.catch { e ->
        Log.e("UserService", "Error fetching user names", e)

    }
}