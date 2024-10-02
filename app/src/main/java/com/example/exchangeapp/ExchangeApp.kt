package com.example.exchangeapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.ExchangeAppTheme
import com.example.exchangeapp.screens.navigation.NavigationScreen
import com.example.exchangeapp.screens.sign_in.SignInScreen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExchangeApp(){
    ExchangeAppTheme {
        Surface (color = MaterialTheme.colorScheme.background){
            val appState = rememberAppState()

            Scaffold { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = INFORMATION_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ){
                    exchangeGraph(appState)
                }
            }

        }
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        ExchangeAppState(navController)
    }

fun NavGraphBuilder.exchangeGraph(appState: ExchangeAppState){
    composable (NAVIGATION_SCREEN){
        NavigationScreen()
    }

    composable(SIGN_IN_SCREEN){
        SignInScreen(openAndPopUp = {route, popUp->appState.navigateAndPopUp(route, popUp)})

    }

    composable(SIGN_UP_SCREEN){

    }

    composable(SPLASH_SCREEN){

    }


}