package com.example.exchangeapp.screens.information

import com.example.exchangeapp.INFO_SUB_SCREEN1
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor() : ExchangeAppViewModel() {

    fun onMenuClick(open: (String) -> Unit) {
        launchCatching {
            open(MENU_SCREEN)
        }
    }

    fun onSubViewClick(name : String, open: (String) -> Unit){
        open("$INFO_SUB_SCREEN1/$name")

    }

    private val db = FirebaseFirestore.getInstance()

    fun getTitles(title : String, onDataReceived: (String, String) -> Unit){

        var titleRef = db.collection("adapting_tips").document("E7t32f5jyCijoZQXIJsf")

        titleRef.get().addOnSuccessListener { document ->
            if (document != null) {
                // Accessing the title field
                var title = document.getString("title")
                var description = document.getString("description")
                if (title != null && description != null) {
                    onDataReceived(title,description)
                }
            } else {
                println("No such document!")
            }
        }.addOnFailureListener { exception ->
            println("Error getting document: $exception")
        }
    }
}