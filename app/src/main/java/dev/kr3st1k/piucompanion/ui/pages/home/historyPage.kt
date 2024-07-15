package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.viewmodels.HistoryViewModel
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.LazyScores
import dev.kr3st1k.piucompanion.ui.pages.Screen

@Composable
fun HistoryPage(
    navController: NavController,
    viewModel: HistoryViewModel,
    listState: LazyGridState,
) {
    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    if (viewModel.needAuth.value)
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
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