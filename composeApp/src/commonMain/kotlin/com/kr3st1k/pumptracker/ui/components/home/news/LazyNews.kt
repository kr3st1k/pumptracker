package com.kr3st1k.pumptracker.ui.components.home.news

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kr3st1k.pumptracker.core.network.data.news.News
import com.kr3st1k.pumptracker.core.network.data.news.NewsBanner
import com.kr3st1k.pumptracker.getPlatform
import com.kr3st1k.pumptracker.ScrollBarForDesktop
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpeenMeRightRoundBabyRightRoundTop
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyNews(
    news: MutableList<News>,
    newsBanners: MutableList<NewsBanner>,
    onRefresh: () -> Unit,
    listState: LazyListState,
    isRefreshing: Boolean,
) {

    val state = rememberPullToRefreshState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pullToRefresh(
                    enabled = getPlatform().type != "Desktop",
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                )
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    if (newsBanners.isNotEmpty())
                        NewsSlider(newsBanners = newsBanners)
                    else
                        YouSpinMeRightRoundBabyRightRound()
                }
            }
            items(news.toList(), key = {it.id}) { data ->
                NewsThumbnail(news = data)
            }
        }
        YouSpeenMeRightRoundBabyRightRoundTop(state, isRefreshing)
        if (getPlatform().type == "Desktop") {
            ScrollBarForDesktop(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
                    .fillMaxHeight(),
                listState
            )
        }
    }


}
