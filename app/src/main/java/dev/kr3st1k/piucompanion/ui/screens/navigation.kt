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
import dev.kr3st1k.piucompanion.ui.screens.home.news.NewsScreen
import dev.kr3st1k.piucompanion.ui.screens.home.scores.best.BestUserPage
import dev.kr3st1k.piucompanion.ui.screens.home.scores.history.HistoryPage
import dev.kr3st1k.piucompanion.ui.screens.home.user.UserScreen
import dev.kr3st1k.piucompanion.ui.screens.login.AuthLoadingPage
import dev.kr3st1k.piucompanion.ui.screens.login.LoginPage

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
                    viewModel = viewModel(),
                    navController = navController
                )
            }

            composable(route = Screen.NewsPage.route) {
                onNavigateShowBottomBar()
                NewsScreen(
                    navController = navController,
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


