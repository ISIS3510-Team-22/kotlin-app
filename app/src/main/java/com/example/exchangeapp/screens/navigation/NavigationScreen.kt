package com.example.exchangeapp.screens.navigation

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

val items = listOf(
    BottomNavItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        title = "Email",
        selectedIcon = Icons.Filled.Email,
        unselectedIcon = Icons.Outlined.Email
    ),
    BottomNavItem(
        title = "Person",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationScreen() {
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Surface {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                            },
                            label = { Text(item.title) },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title
                                )
                            }
                        )
                    }
                }
            }
        ) {
            when(selectedItemIndex){
                0 -> HomeScreen()
                1 -> EmailScreen()
                2 -> ProfileScreen()
            }

        }
    }

}

@Composable
fun HomeScreen() {
    Text("Home Screen Content")
}

@Composable
fun EmailScreen() {
    Text("Email Screen Content")
}

@Composable
fun ProfileScreen() {
    Text("Profile Screen Content")
}