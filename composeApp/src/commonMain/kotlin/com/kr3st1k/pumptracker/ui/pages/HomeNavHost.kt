package com.kr3st1k.pumptracker.ui.pages

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kr3st1k.pumptracker.ui.pages.home.*
import com.kr3st1k.pumptracker.core.viewmodels.*
import com.kr3st1k.pumptracker.di.LoginManager

@Composable
fun HomeNavHost(
    modifier: Modifier,
    navController: NavHostController,
    listState: LazyGridState
) {
    var startDestination = Screen.LoginPage.route

    if (LoginManager().hasLoginData())
        startDestination = Screen.AuthLoadingPage.route

    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(route = Screen.LoginPage.route) {
            val viewModel = viewModel<LoginViewModel>() {
                LoginViewModel()
            }
            LoginScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(route = Screen.AuthLoadingPage.route) {
            AuthLoadingScreen(
                navController = navController
            )
        }

        composable(route = Screen.NewsPage.route) {
            val viewModel = viewModel<NewsViewModel> {
                NewsViewModel()
            }
            NewsScreen(
                viewModel = viewModel,
                listState = rememberLazyListState()
            )
        }

        composable(route = Screen.UserPage.route) {
            val viewModel = viewModel<UserViewModel> {
                UserViewModel()
            }
            UserScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(route = Screen.HistoryPage.route) {
            HistoryScreen(
                navController = navController,
                listState = listState
            )
        }

        composable(route = Screen.PumbilityPage.route) {
            val viewModel = viewModel<PumbilityViewModel> {
                PumbilityViewModel()
            }
            PumbilityScreen(
                viewModel = viewModel,
                navController = navController,
                listState = listState
            )
        }

        composable(route = Screen.SettingsPage.route) {
            SettingsScreen(
                navController = navController
            )
        }

        composable(route = Screen.TitleShopPage.route) {
            val viewModel = viewModel<TitleShopViewModel> {
                TitleShopViewModel()
            }
            TitleShopScreen(
                viewModel = viewModel,
                navController = navController,
                listState = listState
            )
        }

        composable(route = Screen.AvatarShopPage.route) {
            val viewModel = viewModel<AvatarShopViewModel> {
                AvatarShopViewModel()
            }
            AvatarShopScreen(
                viewModel = viewModel,
                navController = navController,
                listState = listState
            )
        }

        composable(route = Screen.BestUserPage.route) {
            val viewModel = viewModel<BestUserViewModel> {
                BestUserViewModel()
            }
            BestUserScreen(
                viewModel = viewModel,
                navController = navController,
                listState = listState
            )
        }
    }

}


