package com.example.exchangeapp.screens.information.subview2

import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchBarScreenViewModel @Inject constructor() : ExchangeAppViewModel(){
    private val db = FirebaseFirestore.getInstance()

    fun getDocumentsData(collection : String,onDataReceived: (List<Map<String, Any>>) -> Unit) {
        val collectionRef = db.collection(collection)

        collectionRef.get().addOnSuccessListener { documents ->
            val dataList = mutableListOf<Map<String, Any>>()

            for (document in documents) {
                val data = document.data // This retrieves all fields as a Map<String, Any>
                dataList.add(data) // Add the map of fields to the list
            }

            // Pass the list of maps back to the caller
            onDataReceived(dataList)
        }.addOnFailureListener { exception ->
            println("Error getting documents: $exception")
        }
    }

}