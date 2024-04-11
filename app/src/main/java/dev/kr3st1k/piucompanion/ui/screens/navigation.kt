package dev.kr3st1k.piucompanion.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kr3st1k.piucompanion.core.modules.LoginManager
import dev.kr3st1k.piucompanion.ui.screens.home.AuthLoadingPage
import dev.kr3st1k.piucompanion.ui.screens.home.BestUserPage
import dev.kr3st1k.piucompanion.ui.screens.home.HistoryPage
import dev.kr3st1k.piucompanion.ui.screens.home.LoginPage
import dev.kr3st1k.piucompanion.ui.screens.home.NewsScreen
import dev.kr3st1k.piucompanion.ui.screens.home.SettingsPage
import dev.kr3st1k.piucompanion.ui.screens.home.UserScreen

@Composable
fun HomeNavHost(
    modifier: Modifier,
    navController: NavHostController,
    listState: LazyListState,
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
                LoginPage(
                    navController = navController,
                    viewModel = viewModel()
                )
            }

            composable(route = Screen.AuthLoadingPage.route)
            {
                AuthLoadingPage(
                    navController = navController,
                    viewModel = viewModel(),
                )
            }

            composable(route = Screen.NewsPage.route) {
                NewsScreen(
                    viewModel = viewModel(),
                    listState = listState
                )
            }

            composable(route = Screen.UserPage.route) {
                UserScreen(
                    navController = navController,
                    viewModel = viewModel()
                )
            }

            composable(route = Screen.HistoryPage.route) {
                HistoryPage(
                    navController = navController,
                    viewModel = viewModel(),
                    listState = listState
                )
            }

            composable(route = Screen.SettingsPage.route) {
                SettingsPage(
                    navController = navController
                )
            }

            composable(route = Screen.BestUserPage.route) {
                BestUserPage(
                    navController = navController,
                    viewModel = viewModel(),
                    listState = listState
                )
            }
        }
    }
}


