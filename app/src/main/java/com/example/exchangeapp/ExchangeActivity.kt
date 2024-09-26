package com.example.exchangeapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.BuildConfig
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureFirebaseServices()

        setContent { ExchangeApp() }
    }

    private fun configureFirebaseServices() {
//        if (BuildConfig.DEBUG) {
//            Firebase.auth.useEmulator(LOCALHOST, AUTH_PORT)
//            Firebase.firestore.useEmulator(LOCALHOST, FIRESTORE_PORT)
//        }
    }
}