package dev.kr3st1k.piucompanion.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kr3st1k.piucompanion.core.prefs.LoginManager
import dev.kr3st1k.piucompanion.ui.screens.home.AuthLoadingPage
import dev.kr3st1k.piucompanion.ui.screens.home.LoginPage
import dev.kr3st1k.piucompanion.ui.screens.home.NewsScreen
import dev.kr3st1k.piucompanion.ui.screens.home.UserScreen
import dev.kr3st1k.piucompanion.ui.screens.home.scores.BestUserPage
import dev.kr3st1k.piucompanion.ui.screens.home.scores.HistoryPage

@Composable
fun HomeNavHost(
    modifier: Modifier,
    navController: NavHostController,
    onNavigateShowBottomBar: () -> Unit,
    onNavigateNotShowBottomBar: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var startDestination = Screen.LoginPage.route

    if (LoginManager().hasLoginData())
        startDestination = Screen.AuthLoadingPage.route


    BackHandler(enabled = true) {

    }
    CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
        NavHost(
            navController = navController, startDestination = startDestination, modifier = modifier
        ) {
            composable(route = Screen.LoginPage.route) {
                onNavigateNotShowBottomBar()
                LoginPage(
                    navController = navController,
                    viewModel = viewModel()
                )
            }

            composable(route = Screen.AuthLoadingPage.route)
            {
                onNavigateNotShowBottomBar()
                AuthLoadingPage(
                    navController = navController,
                    viewModel = viewModel(),
                )
            }

            composable(route = Screen.NewsPage.route) {
                onNavigateShowBottomBar()
                NewsScreen(
                    lifecycleOwner = lifecycleOwner
                )
            }

            composable(route = Screen.UserPage.route) {
                onNavigateShowBottomBar()
                UserScreen(
                    navController = navController,
                    lifecycleOwner = lifecycleOwner
                )
            }

            composable(route = Screen.HistoryPage.route) {
                onNavigateShowBottomBar()
                HistoryPage(
                    navController = navController,
                    lifecycleOwner = lifecycleOwner
                )
            }

            composable(route = Screen.BestUserPage.route) {
                onNavigateShowBottomBar()
                BestUserPage(
                    navController = navController,
                    lifecycleOwner = lifecycleOwner
                )
            }
        }
    }
}


