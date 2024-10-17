package com.example.exchangeapp.screens.information.subview1

import android.content.ContentValues.TAG
import android.util.Log
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BasicScreenViewModel @Inject constructor() : ExchangeAppViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun getContent(colname : String){
        var docs = db.collection(colname)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    fun getDocument(onDataReceived: (String, String) -> Unit){

        var docRef = db.collection("adapting_tips").document("E7t32f5jyCijoZQXIJsf")

        docRef.get().addOnSuccessListener { document ->
            Log.d("getSuccess", "Entro al get")
            if (document != null) {
                // Accessing the title field
                var title = document.getString("title")
                var description = document.getString("description")
                if (title != null && description != null) {
                        onDataReceived(title,description)
                }
                Log.d("GetTest", "info retrieved with $title and $description")
            } else {
                println("No such document!")
            }
        }.addOnFailureListener { exception ->
            println("Error getting document: $exception")
        }
    }

    fun getDocumentsData(onDataReceived: (List<Map<String, Any>>) -> Unit) {
        val collectionRef = db.collection("yourCollection")

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