package com.kr3st1k.pumptracker.nav.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.kr3st1k.pumptracker.nav.refreshFunction
import com.kr3st1k.pumptracker.ui.components.home.scores.LazyScores
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@Composable
fun HistoryComponentImpl(viewModel: HistoryComponent, listState: LazyGridState) {
    val scores = viewModel.scores
    val isRefreshing by viewModel.isRefreshing
    refreshFunction.value = { viewModel.fetchAndAddToDb() }

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