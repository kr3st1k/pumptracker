package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kr3st1k.piucompanion.core.viewmodels.HistoryViewModel
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.LazyLatestScore

@Composable
fun HistoryPage(
    viewModel: HistoryViewModel,
    listState: LazyGridState,
) {
    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    LazyLatestScore(
        scores,
        onRefresh = { viewModel.fetchAndAddToDb() },
        listState = listState,
        isRefreshing = isRefreshing
    )
    if (scores.isEmpty()) {
        YouSpinMeRightRoundBabyRightRound("Getting latest scores...")
    }
}