package dev.kr3st1k.piucompanion.ui.components.home.scores

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LazyBestScore(
    scores: List<BestUserScore>,
    onRefresh: () -> Unit,
    onLoadNext: () -> Unit,
    isLoadMoreFlow: StateFlow<Boolean>,
    dropDownMenu: @Composable () -> Unit,
    listState: LazyListState,
) {
    val isRefreshing by remember {
        mutableStateOf(false)
    }
    val state = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    val isLoadMore by isLoadMoreFlow.collectAsStateWithLifecycle()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                lastVisibleItem?.let {
                    if (it.index == listState.layoutInfo.totalItemsCount - 1) {
                        if (isLoadMore) {
                            onLoadNext()
                        }
                    }
                }
            }
    }
    Box(
        contentAlignment = Alignment.TopCenter,
    ) {
        SwipeRefresh(
            state = state,
            onRefresh = onRefresh
        ) {
            LazyColumn(state = listState) {
                item {
                    dropDownMenu()
                }
                items(scores) { data ->
                    ScoreCard(data)
                    if (scores.indexOf(data) == scores.count() - 1 && isLoadMore)
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Spacer(modifier = Modifier.size(2.dp))
                            CircularProgressIndicator()
                        }
                }
            }
        }
    }
}