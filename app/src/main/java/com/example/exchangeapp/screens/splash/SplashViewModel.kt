package com.example.exchangeapp.screens.splash


import com.example.exchangeapp.NAVIGATION_SCREEN
import com.example.exchangeapp.SIGN_IN_SCREEN
import com.example.exchangeapp.SPLASH_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountService: AccountService
) : ExchangeAppViewModel() {

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (accountService.hasUser()) openAndPopUp(NAVIGATION_SCREEN, SPLASH_SCREEN)
        else openAndPopUp(SIGN_IN_SCREEN, SPLASH_SCREEN)
    }
}