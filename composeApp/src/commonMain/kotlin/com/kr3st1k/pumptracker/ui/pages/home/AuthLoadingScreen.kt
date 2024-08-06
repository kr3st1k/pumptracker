@file:Suppress("FunctionName")

package com.kr3st1k.pumptracker.ui.pages.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kr3st1k.pumptracker.core.viewmodels.AuthViewModel
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound
import com.kr3st1k.pumptracker.ui.pages.Screen

@Composable
fun AuthLoadingScreen(navController: NavController) {
    val viewModel = remember {
        AuthViewModel()
    }
    Box(modifier = Modifier.fillMaxSize())
    {
        YouSpinMeRightRoundBabyRightRound("Authorizing...")
    }
    if (!viewModel.isLoading.value)
        if (viewModel.isFailed.value) {
            viewModel.isLoading.value = true // КАСТЫЛЬ БЛИН
            navController.navigate(Screen.LoginPage.route) {
                popUpTo(navController.graph.id)
                {
                    saveState = false
                    inclusive = true
                }
            }
        }
        else {
            viewModel.isLoading.value = true // КАСТЫЛЬ БЛИН
            navController.navigate(Screen.HistoryPage.route) {
                popUpTo(navController.graph.id)
                {
                    saveState = false
                    inclusive = true
                }
            }
        }
}

