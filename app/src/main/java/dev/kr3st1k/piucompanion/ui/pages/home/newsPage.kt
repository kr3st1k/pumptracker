package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kr3st1k.piucompanion.core.viewmodels.NewsViewModel
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.news.LazyNews

@Composable
fun NewsScreen(
    viewModel: NewsViewModel,
    listState: LazyListState,
) {
    val newsBanners by viewModel.newsBanners.collectAsStateWithLifecycle()
    val news = viewModel.news.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

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