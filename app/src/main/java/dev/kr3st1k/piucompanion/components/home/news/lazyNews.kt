package dev.kr3st1k.piucompanion.components.home.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyNews(news: MutableList<NewsThumbnailObject>, onRefresh: () -> Unit) {
    val isRefreshing by remember {
        mutableStateOf(false)
    }

    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Box (
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(modifier = Modifier.pullRefresh(state)) {
            items(news.toList()) { data ->
                NewsThumbnail(news = data)
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = state
        )
    }

}