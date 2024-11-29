package com.example.exchangeapp.screens.universities

import android.util.Log
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class UniversityViewModel @Inject constructor() : ExchangeAppViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun displayDetails(university : String, open: (String) -> Unit){
        open(university)
    }

    val documentsCache = MutableStateFlow<List<Map<String, Any>>?>(null)

    fun getDocumentsData(collection: String, onDataReceived: (List<Map<String, Any>>) -> Unit) {
        // Check if the data is already cached
        if (documentsCache.value != null) {
            // If data is cached, pass it to the callback
            onDataReceived(documentsCache.value!!)
            return
        }

        // Fetch from Firestore if not cached
        val collectionRef = db.collection(collection)

        collectionRef.get()
            .addOnSuccessListener { documents ->
                val dataList = mutableListOf<Map<String, Any>>()

                for (document in documents) {
                    val data = document.data // Retrieve all fields as a Map<String, Any>
                    dataList.add(data)
                }

                // Cache the fetched data
                documentsCache.value = dataList
                Log.d("cache data", documentsCache.value.toString())

                // Pass the data to the callback
                onDataReceived(dataList)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}