package com.kr3st1k.pumptracker.nav.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.kr3st1k.pumptracker.ui.components.home.scores.LazyScores
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@Composable
fun HistoryComponentImpl(viewModel: HistoryComponent) {
    val scores = viewModel.scores
    val isRefreshing by viewModel.isRefreshing
    val listState = rememberLazyGridState()
    val tappedState by viewModel.isScrollable.subscribeAsState()

    LaunchedEffect(tappedState) {
        listState.animateScrollToItem(0)
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