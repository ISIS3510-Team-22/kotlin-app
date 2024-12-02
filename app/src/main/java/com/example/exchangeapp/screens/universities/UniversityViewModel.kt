package com.example.exchangeapp.screens.universities

import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.ADD_COMMENT_SCREEN
import com.example.exchangeapp.DataStorage.SharedPreferencesManager
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UniversityViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ExchangeAppViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun displayDetails(university: String, open: (String) -> Unit) {
        open("$ADD_COMMENT_SCREEN/$university")
    }

    fun getDocument(university: String, onDataReceived: (Map<String, Any>) -> Unit) {
        val docs = db.collection("universities")

        docs.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = mutableMapOf<String, Any>()

                if (document != null) {
                    val name = document.getString("name")
                    val city = document.getString("city")
                    val country = document.getString("country")

                    // Handle the 'students' field safely
                    val students = document.get("students") // Generic getter
                    val studentsFormatted = when (students) {
                        is String -> students
                        is Long -> "$students" // Convert number to string
                        is Double -> "$students" // Handle decimal numbers
                        else -> "N/A" // Fallback if the field is missing or another type
                    }

                    if (name == university) {
                        data["name"] = name ?: "Unknown"
                        data["city"] = city ?: "Unknown"
                        data["country"] = country ?: "Unknown"
                        data["students"] = studentsFormatted

                        onDataReceived(data)
                    }
                } else {
                    println("No such document!")
                }
            }
        }.addOnFailureListener { exception ->
            println("Error getting document: $exception")
        }
    }

    fun saveLastViewedUniversity(university: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sharedPreferencesManager.saveLastViewedUniversity(university)
            }
        }
    }

    fun loadLastViewedUniversity(): String? {
        return sharedPreferencesManager.loadLastViewedUniversity()
    }

}