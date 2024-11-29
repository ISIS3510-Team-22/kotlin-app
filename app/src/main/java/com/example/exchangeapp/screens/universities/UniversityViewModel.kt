package com.example.exchangeapp.screens.universities

import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UniversityViewModel @Inject constructor() : ExchangeAppViewModel() {

    fun displayDetils(university : String, open: (String) -> Unit){
        open(university)
    }
}