package dev.kr3st1k.piucompanion.ui.components.home.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound

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
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pullToRefresh(
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh
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
            items(news.toList()) { data ->
                NewsThumbnail(news = data)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            if (news.isNotEmpty())
                PullToRefreshDefaults.Indicator(state = state, isRefreshing = isRefreshing)
        }
    }


}
