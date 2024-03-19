package dev.kr3st1k.piucompanion.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.screens.Screen
import dev.kr3st1k.piucompanion.screens.components.home.HomeBottomBar
import dev.kr3st1k.piucompanion.objects.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateTo: String
) {
    val topLevelDestinations = listOf(
        TopLevelDestination(
            route = Screen.NewsPage.route,
            selectedIcon = Icons.Rounded.Email,
            unselectedIcon = Icons.Outlined.Email,
            iconText = "News"
        )
    )

    val showBottomBar = remember { mutableStateOf(true) }
    val title = remember {
        mutableStateOf("Home")
    }
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = title.value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            })
        },
        bottomBar = {
            if (showBottomBar.value) {
                HomeBottomBar(destinations = topLevelDestinations,
                    currentDestination = navController.currentBackStackEntryAsState().value?.destination,
                    onNavigateToDestination = {
                        title.value = when (it) {
                            "sc1" -> "Screen 1"
                            "sc2" -> "Screen 2"
                            else -> {
                                "Screen 3"
                            }
                        }
                        navController.navigate(it) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    })
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            HomeNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                navController = navController,
                startDestination = Screen.NewsPage.route
            )

        }
    }
}
