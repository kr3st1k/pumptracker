package com.kr3st1k.pumptracker.ui.pages.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kr3st1k.pumptracker.core.viewmodels.HistoryViewModel
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound
import com.kr3st1k.pumptracker.ui.components.home.scores.LazyScores
import com.kr3st1k.pumptracker.ui.pages.Screen
import com.kr3st1k.pumptracker.ui.pages.currentPage
import com.kr3st1k.pumptracker.ui.pages.navigateUp
import com.kr3st1k.pumptracker.ui.pages.refreshFunction

@Composable
fun HistoryScreen(
    navController: NavController,
    listState: LazyGridState,
) {
    val viewModel = remember { HistoryViewModel() }

    val scores = viewModel.scores
    val isRefreshing by viewModel.isRefreshing

    refreshFunction.value = { viewModel.fetchAndAddToDb() }
    
    if (viewModel.needAuth.value) {
        currentPage = null
        navigateUp = null
        viewModel.needAuth.value = false
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyScores(
            scores,
            onRefresh = { viewModel.fetchAndAddToDb() },
            listState = listState,
            isRefreshing = isRefreshing
        )
        if (scores.isEmpty()) {
            YouSpinMeRightRoundBabyRightRound("Getting latest scores...")
        }
    }
}