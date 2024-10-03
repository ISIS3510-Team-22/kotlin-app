package com.example.exchangeapp.model.service.impl

import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AccountServiceImpl @Inject constructor() : AccountService {

    private val db = FirebaseFirestore.getInstance()



    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid) })
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }
    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String) {
        val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
        authResult.user?.let { firebaseUser ->
            val userId = firebaseUser.uid
            val userDocument = db.collection("users").document(userId).get().await()


            if (!userDocument.exists()) {


                val newUser = User(id = userId, name = firebaseUser.email ?: "No name", lat = 0.0, long = 0.0)
                saveUserInFirestore(newUser)
            }


        }
    }

    override suspend fun signUp(email: String, password: String) {

       val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        authResult.user?.let {firebaseUser ->
            val newUser = User(id = firebaseUser.uid, name = firebaseUser.email ?: "No name", lat = 0.0, long = 0.0)
            saveUserInFirestore(newUser)
        }
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }

    private suspend fun saveUserInFirestore(user: User) {
        db.collection("users").document(user.id).set(user).await()
    }






}