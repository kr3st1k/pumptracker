package dev.kr3st1k.piucompanion.screens.auth.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kr3st1k.piucompanion.screens.Screen
import dev.kr3st1k.piucompanion.screens.auth.best.BestUserPage
import dev.kr3st1k.piucompanion.screens.auth.history.HistoryPage
import dev.kr3st1k.piucompanion.screens.auth.user.UserScreen
import dev.kr3st1k.piucompanion.screens.unauth.news.NewsScreen

@Composable
fun HomeNavHost(
    modifier: Modifier,
    navController: NavHostController,
    navControllerGlobal: NavController,
    startDestination: String,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
        NavHost(
            navController = navController, startDestination = startDestination, modifier = modifier
        ) {

            composable(route = Screen.NewsPage.route) {
                NewsScreen(navController = navController, lifecycleOwner)
            }

            composable(route = Screen.UserPage.route) {
                UserScreen(
                    navController = navController,
                    navControllerGlobal = navControllerGlobal,
                    lifecycleOwner = lifecycleOwner
                )
            }

            composable(route = Screen.HistoryPage.route) {
                HistoryPage(
                    navControllerGlobal = navControllerGlobal,
                    lifecycleOwner = lifecycleOwner
                )
            }

            composable(route = Screen.BestUserPage.route) {
                BestUserPage(navControllerGlobal = navControllerGlobal)
            }
        }
    }
}
