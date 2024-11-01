package com.example.exchangeapp

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import com.example.exchangeapp.ui.theme.ExchangeAppTheme
import com.example.exchangeapp.screens.auth.forgot_password.ForgotPasswordScreen
import com.example.exchangeapp.screens.auth.sign_in.SignInScreen
import com.example.exchangeapp.screens.auth.sign_up.SignUpScreen
import com.example.exchangeapp.screens.chat.ChatScreen
import com.example.exchangeapp.screens.chatpreview.ChatPreviewScreen
import com.example.exchangeapp.screens.information.InformationScreen
import com.example.exchangeapp.screens.information.subviews.BasicScreen
import com.example.exchangeapp.screens.information.subviews.SearchBarScreen
import com.example.exchangeapp.screens.menu.MenuScreen
import com.example.exchangeapp.screens.navigation.NavigationScreen
import com.example.exchangeapp.screens.profile.ProfileScreen
import com.example.exchangeapp.screens.splash.SplashScreen
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
@OptIn(ExperimentalMaterial3Api::class)

fun ExchangeApp(fusedLocationProviderClient: FusedLocationProviderClient) {
    ExchangeAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()


            Scaffold { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,

                    startDestination = SPLASH_SCREEN,

                    enterTransition = { EnterTransition.None },

                    exitTransition = { ExitTransition.None },

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


fun NavGraphBuilder.exchangeGraph(appState: ExchangeAppState) {
    composable(NAVIGATION_SCREEN) {
        NavigationScreen(appState)
    }

    composable(SIGN_IN_SCREEN) {
        SignInScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            open = { route -> appState.navigate(route) })

    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            popUp = { appState.popUp() })

    }

    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })

    }

    composable(CHAT_PREVIEW_SCREEN) {
        ChatPreviewScreen(open = { route -> appState.navigate(route) })
    }

    composable("$CHAT_SCREEN/{userName}") { backStackEntry ->
        val userName = backStackEntry.arguments?.getString("userName") ?: "Unknown"
        ChatScreen(popUp = { appState.popUp() }, receiverName = userName)
    }

    composable(FORGOT_PASSWORD_SCREEN) {
        ForgotPasswordScreen(
            popUp = { appState.popUp() }
        )
    }

    composable(INFO_SCREEN) {
        InformationScreen(
            open = { route -> appState.navigate(route) },
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable(MENU_SCREEN) {
        MenuScreen(
            popUp = { appState.popUp() },
            clearAndNavigate = { route -> appState.clearAndNavigate(route) },
            open = { route -> appState.navigate(route) }
        )
    }

    composable("$INFO_SUB_SCREEN1/{name}"){ backStackEntry ->
        var name = backStackEntry.arguments?.getString("name") ?: "Unknown"
        BasicScreen(name = name , popUp = { appState.popUp() })
    }

    composable("$INFO_SUB_SCREEN2/{name}"){ backStackEntry ->
        var name = backStackEntry.arguments?.getString("name") ?: "Unknown"
        SearchBarScreen(name = name , popUp = { appState.popUp() })
    }

    composable(PROFILE_SCREEN){
        ProfileScreen()
    }
}