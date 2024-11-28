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

    fun getCurrentUser(userId: String): Flow<User> = flow {
        val document = firestore.collection("users").document(userId).get().await()
        val name = document.getString("name") ?: ""
        val email = document.getString("email") ?: ""
        val profilePictureUrl = document.getString("profilePictureUrl")
        val lat = document.getDouble("lat") ?: 0.0
        val long = document.getDouble("long") ?: 0.0

        val user = User(
            id = userId,
            name = name,
            email = email,
            profilePictureUrl = profilePictureUrl,
            lat = lat,
            long = long
        )
        emit(user)
    }.catch { e ->
        Log.e("UserService", "Error fetching current user", e)
    }

    fun updateProfilePictureUrl(userId: String, newProfilePictureUrl: String): Flow<Boolean> = flow {
        Log.d("IMAGEUPLOAD", "userId: $userId")

        try {
            val userRef = firestore.collection("users").document(userId)
            Log.d("IMAGEUPLOAD", "updateProfilePictureUrl: $userId")

            // Update the profilePictureUrl field
            userRef.update("profilePictureUrl", newProfilePictureUrl).await()

            // Emit success result
            emit(true)
        } catch (e: Exception) {
            // Log error and emit failure result
            Log.e("UserService", "Error updating profile picture URL", e)
            emit(false)
        }
    }




}