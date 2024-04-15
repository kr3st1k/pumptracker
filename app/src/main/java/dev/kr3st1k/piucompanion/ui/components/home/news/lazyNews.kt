package dev.kr3st1k.piucompanion.ui.components.home.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound

@Composable
fun LazyNews(
    news: MutableList<News>,
    newsBanners: MutableList<NewsBanner>,
    onRefresh: () -> Unit,
    listState: LazyListState,
) {
    val isRefreshing by remember {
        mutableStateOf(false)
    }

    val state = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    Box (
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxWidth()
    ) {
        SwipeRefresh(
            state = state,
            onRefresh = onRefresh
        ) {
            LazyColumn(state = listState) {
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
        }

//        PullRefreshIndicator(
//            refreshing = isRefreshing,
//            state = state
//        )
    }

}
