package com.kr3st1k.pumptracker.nav.news

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.kr3st1k.pumptracker.ui.components.home.news.LazyNews
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@Composable
fun NewsComponentImpl(viewModel: NewsComponent) {
    val newsBanners by viewModel.newsBanners.collectAsStateWithLifecycle()
    val news = viewModel.news.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val tappedState by viewModel.isScrollable.subscribeAsState()

    LaunchedEffect(tappedState) {
        listState.animateScrollToItem(0)
    }

    LazyNews(
        news = news.value,
        newsBanners = newsBanners,
        listState = listState,
        onRefresh = { viewModel.refreshNews() },
        isRefreshing = isRefreshing
    )
    if (news.value.isEmpty()) {
        YouSpinMeRightRoundBabyRightRound("Getting news...")
    }
}