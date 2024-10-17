package com.example.exchangeapp.model.service.impl


import android.util.Log
import com.example.exchangeapp.model.service.User
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

    fun getUsers(): Flow<List<User>> = flow {
        val userList = mutableListOf<User>()
        val querySnapshot = firestore.collection("users").get().await()
        for (document in querySnapshot.documents) {
            val id = document.id
            val name = document.getString("name") ?: ""
            val email = document.getString("email") ?: ""
            val profilePictureUrl = document.getString("profilePictureUrl")
            val lat = document.getDouble("lat") ?: 0.0
            val long = document.getDouble("long") ?: 0.0

            val user = User(
                id = id,
                name = name,
                email = email,
                profilePictureUrl = profilePictureUrl,
                lat = lat,
                long = long
            )
            userList.add(user)
        }
        emit(userList)
    }.catch { e ->
        Log.e("UserService", "Error fetching users", e)
    }



}