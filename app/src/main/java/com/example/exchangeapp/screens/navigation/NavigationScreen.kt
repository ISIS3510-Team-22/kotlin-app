package com.example.exchangeapp.screens.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.exchangeapp.ExchangeAppState
import com.example.exchangeapp.R
import com.example.exchangeapp.screens.aichat.AiChatScreen
import com.example.exchangeapp.screens.chatpreview.ChatPreviewScreen
import com.example.exchangeapp.screens.information.InformationScreen
import com.example.exchangeapp.screens.news.NewsScreen

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    val selectedIconRes: Int? = null,  // For drawable resources
    val unselectedIconRes: Int? = null,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

val colorBg = Color(0xFF0F3048)

val items = listOf(
    BottomNavItem(
        title = "Info",
        selectedIcon = Icons.AutoMirrored.Filled.StickyNote2,
        unselectedIcon = Icons.AutoMirrored.Outlined.StickyNote2
    ),
    BottomNavItem(
        title = "Chat",
        selectedIcon = Icons.Filled.Email,
        unselectedIcon = Icons.Outlined.Email
    ),
    BottomNavItem(
        title = "News",
        selectedIconRes = R.drawable.world,
        unselectedIconRes = R.drawable.world
    ),

    BottomNavItem(
        title = "AI help",
        selectedIconRes = R.drawable.ai_bot,
        unselectedIconRes = R.drawable.ai_bot
    )


)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavigationScreen(appState: ExchangeAppState) {
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }


    Surface {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    items.forEachIndexed { index, item ->
                        val iconSize by animateFloatAsState(
                            targetValue = if (selectedItemIndex == index) 30f else 24f,
                            animationSpec = tween(durationMillis = 200)
                        )
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = colorBg,
                                indicatorColor = colorBg
                            ),
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                            },
                            label = {
                                Text(item.title)
                            },
                            icon = {
                                MyIcon(
                                    selectedItemIndex = selectedItemIndex,
                                    index = index,
                                    item = item,
                                    iconSize = iconSize
                                )
                            }
                        )

                    }
                }
            }
        ) {
            when (selectedItemIndex) {
                0 -> InformationScreen(
                    open = { route -> appState.navigate(route) },
                    openAndPopUp = { route, popUp ->
                        appState.navigateAndPopUp(
                            route,
                            popUp
                        )
                    })

                1 -> ChatPreviewScreen(open = { route -> appState.navigate(route) })
                2 -> NewsScreen(open = { route -> appState.navigate(route) })
                3 -> AiChatScreen(open = { route -> appState.navigate(route) })
                }

        }
    }

}

@Composable
fun MyIcon(selectedItemIndex: Int, index: Int, item: BottomNavItem, iconSize: Float) {
    if (selectedItemIndex == index) {
        // Load selected drawable resource or vector icon
        if (item.selectedIconRes != null) {
            Icon(
                painter = painterResource(id = item.selectedIconRes),
                contentDescription = item.title,
                modifier = Modifier.size(iconSize.dp)
            )
        } else {
            Icon(
                imageVector = item.selectedIcon
                    ?: Icons.Default.Home,  // Fallback icon
                contentDescription = item.title,
                modifier = Modifier.size(iconSize.dp)
            )
        }
    } else {
        // Load unselected drawable resource or vector icon
        if (item.unselectedIconRes != null) {
            Icon(
                painter = painterResource(id = item.unselectedIconRes),
                contentDescription = item.title,
                modifier = Modifier.size(iconSize.dp)
            )
        } else {
            Icon(
                imageVector = item.unselectedIcon
                    ?: Icons.Default.Home,  // Fallback icon
                contentDescription = item.title,
                modifier = Modifier.size(iconSize.dp)
            )
        }
    }
}