package dev.kr3st1k.piucompanion.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.ui.screens.home.HomeScreen
import dev.kr3st1k.piucompanion.ui.screens.home.news.NewsScreen
import dev.kr3st1k.piucompanion.ui.screens.home.scores.best.BestUserPage
import dev.kr3st1k.piucompanion.ui.screens.home.scores.history.HistoryPage
import dev.kr3st1k.piucompanion.ui.screens.home.user.UserScreen
import dev.kr3st1k.piucompanion.ui.screens.login.LoginWebViewScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    val startDist = Screen.HomeScreen.route;

    BackHandler(enabled = true) {

    }
    NavHost(navController = navController, startDestination = startDist)
    {
        composable(route = Screen.LoginWebViewScreen.route) {
            LoginWebViewScreen(navController = navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
    }
}

@Composable
fun HomeNavHost(
    modifier: Modifier,
    navController: NavHostController,
    navControllerGlobal: NavController,
    startDestination: String,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    BackHandler(enabled = true) {

    }
    CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
        NavHost(
            navController = navController, startDestination = startDestination, modifier = modifier
        ) {

            composable(route = Screen.NewsPage.route) {
                NewsScreen(
                    navController = navController,
                    lifecycleOwner = lifecycleOwner
                )
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
                BestUserPage(
                    navControllerGlobal = navControllerGlobal,
                    lifecycleOwner = lifecycleOwner
                )
            }
        }
    }
}


