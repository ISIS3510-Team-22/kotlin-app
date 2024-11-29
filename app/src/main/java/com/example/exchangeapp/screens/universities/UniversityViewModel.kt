package com.example.exchangeapp.screens.universities

import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UniversityViewModel @Inject constructor() : ExchangeAppViewModel() {

    private val db = FirebaseFirestore.getInstance()

    fun displayDetails(university : String, open: (String) -> Unit){
        open(university)
    }

}