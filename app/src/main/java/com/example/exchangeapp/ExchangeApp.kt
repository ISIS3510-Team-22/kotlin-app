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
import com.example.exchangeapp.screens.auth.sign_in.SignInScreen
import com.example.exchangeapp.screens.auth.sign_up.SignUpScreen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExchangeApp() {
    ExchangeAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            Scaffold { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SIGN_IN_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
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

    composable(SIGN_IN_SCREEN) {
        SignInScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }, open={route->appState.navigate(route)})

    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }, popUp = { appState.popUp() })

    }

    composable(SPLASH_SCREEN) {

    }


}