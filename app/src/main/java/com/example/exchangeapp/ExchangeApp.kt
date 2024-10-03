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
import com.example.exchangeapp.screens.chatpreview.ChatPreviewScreen
import com.example.exchangeapp.screens.chat.ChatScreen
import com.example.exchangeapp.screens.navigation.NavigationScreen
import com.example.exchangeapp.screens.auth.sign_in.SignInScreen
import com.example.exchangeapp.screens.auth.sign_up.SignUpScreen
import com.example.exchangeapp.screens.splash.SplashScreen
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExchangeApp(fusedLocationProviderClient: FusedLocationProviderClient){
    ExchangeAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            Scaffold { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,

                    startDestination = SPLASH_SCREEN,

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
        NavigationScreen(appState)
    }

    composable(SIGN_IN_SCREEN) {
        SignInScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }, open={route->appState.navigate(route)})

    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }, popUp = { appState.popUp() })

    }

    composable(SPLASH_SCREEN){
        SplashScreen(openAndPopUp = {route, popUp->appState.navigateAndPopUp(route, popUp)})

    }

    composable(CHAT_PREVIEW_SCREEN) {
        ChatPreviewScreen { contactName ->
            appState.navController.navigate("$CHAT_SCREEN/$contactName")
        }
    }

    composable("$CHAT_SCREEN/{contactName}") { backStackEntry ->
        val contactName = backStackEntry.arguments?.getString("contactName") ?: "Unknown"
        ChatScreen(contactName = contactName)
    }


}