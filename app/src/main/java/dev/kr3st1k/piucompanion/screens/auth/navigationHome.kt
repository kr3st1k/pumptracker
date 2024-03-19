package dev.kr3st1k.piucompanion.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kr3st1k.piucompanion.screens.Screen

@Composable
fun HomeNavHost(
    modifier: Modifier,
    navController: NavHostController,
    navControllerGlobal: NavController,
    startDestination: String
) {

    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {

        composable(route = Screen.NewsPage.route) {
            NewsScreen(navController = navController)
        }

        composable(route = Screen.UserPage.route) {
            UserScreen(navController = navController, navControllerGlobal = navControllerGlobal)
        }

    }
}
