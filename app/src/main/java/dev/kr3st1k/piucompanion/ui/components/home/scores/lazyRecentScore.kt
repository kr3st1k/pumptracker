package dev.kr3st1k.piucompanion.ui.components.home.scores

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.kr3st1k.piucompanion.core.network.data.LatestScore

@Composable
fun LazyLatestScore(
    scores: MutableList<LatestScore>,
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
                items(scores.toList()) { data ->
                    ScoreCard(data)
                }
            }
        }
    }
}
