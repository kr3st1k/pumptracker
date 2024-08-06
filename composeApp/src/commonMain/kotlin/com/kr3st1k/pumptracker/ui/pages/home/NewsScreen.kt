package com.kr3st1k.pumptracker.ui.pages.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kr3st1k.pumptracker.core.viewmodels.NewsViewModel
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound
import com.kr3st1k.pumptracker.ui.components.home.news.LazyNews
import com.kr3st1k.pumptracker.ui.pages.refreshFunction

@Composable
fun NewsScreen(
    viewModel: NewsViewModel,
    listState: LazyListState,
) {
    val newsBanners by viewModel.newsBanners.collectAsStateWithLifecycle()
    val news = viewModel.news.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    refreshFunction.value = { viewModel.refreshNews() }
    
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