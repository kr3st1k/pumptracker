package dev.kr3st1k.piucompanion.ui.pages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.kr3st1k.piucompanion.core.modules.LoginManager
import dev.kr3st1k.piucompanion.ui.pages.home.AuthLoadingPage
import dev.kr3st1k.piucompanion.ui.pages.home.AvatarShopScreen
import dev.kr3st1k.piucompanion.ui.pages.home.BestUserPage
import dev.kr3st1k.piucompanion.ui.pages.home.HistoryPage
import dev.kr3st1k.piucompanion.ui.pages.home.LoginPage
import dev.kr3st1k.piucompanion.ui.pages.home.NewsScreen
import dev.kr3st1k.piucompanion.ui.pages.home.PumbilityScreen
import dev.kr3st1k.piucompanion.ui.pages.home.SettingsPage
import dev.kr3st1k.piucompanion.ui.pages.home.UserScreen
@Composable
fun HomeNavHost(
    modifier: Modifier,
    navController: NavHostController,
    listState: LazyGridState
) {
    var startDestination = Screen.LoginPage.route

    if (LoginManager().hasLoginData())
        startDestination = Screen.AuthLoadingPage.route

    BackHandler(enabled = true) {

    }
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
                    listState = rememberLazyListState()
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
                    viewModel = viewModel(),
                    listState = listState
                )
            }

            composable(route = Screen.PumbilityPage.route) {
                PumbilityScreen(
                    viewModel = viewModel(),
                    listState = listState
                )
            }

            composable(route = Screen.SettingsPage.route) {
                SettingsPage(
                    navController = navController
                )
            }

            composable(route = Screen.AvatarShopPage.route) {
                AvatarShopScreen(
                    viewModel = viewModel(),
                    navController = navController,
                    listState = listState
                )
            }

            composable(route = Screen.BestUserPage.route) {
                BestUserPage(
                    viewModel = viewModel(),
                    listState = listState
                )
            }
        }

}


