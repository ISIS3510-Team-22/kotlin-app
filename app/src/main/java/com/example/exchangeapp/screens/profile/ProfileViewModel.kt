package com.example.exchangeapp.screens.profile

import com.example.exchangeapp.PROFILE_SCREEN
import com.example.exchangeapp.SIGN_UP_SCREEN
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : ExchangeAppViewModel(){

    fun onProfileClick(open: (String) -> Unit) {
        open(PROFILE_SCREEN)
    }

}