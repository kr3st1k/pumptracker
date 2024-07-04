package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.viewmodels.AuthViewModel
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.pages.Screen

@Composable
fun AuthLoadingPage(viewModel: AuthViewModel, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize())
    {
        YouSpinMeRightRoundBabyRightRound("Authorizing...")
    }
    if (!viewModel.isLoading.value)
        if (viewModel.isFailed.value)
            navController.navigate(Screen.LoginPage.route) {
                popUpTo(navController.graph.id)
                {
                    inclusive = true
                }
            }
        else
            navController.navigate(Screen.HistoryPage.route) {
                popUpTo(navController.graph.id)
                {
                    inclusive = true
                }
            }
}

