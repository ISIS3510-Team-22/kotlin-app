package com.example.exchangeapp.screens.menu

import com.example.exchangeapp.PROFILE_SCREEN
import com.example.exchangeapp.SIGN_IN_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.example.exchangeapp.screens.information.subviews.BasicScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val accountService: AccountService
) : ExchangeAppViewModel() {


    fun logOut(clearAndNavigate:(String)->Unit,subViewVM : BasicScreenViewModel ){
        launchCatching {
            accountService.signOut()
            subViewVM.clearDocumentsCache()
            clearAndNavigate(SIGN_IN_SCREEN)

        }
    }

    fun onProfileClick(open: (String) -> Unit) {
        open(PROFILE_SCREEN)
    }
}