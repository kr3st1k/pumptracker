package dev.kr3st1k.piucompanion.ui.components.home.scores

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.flow.StateFlow

// This whole file is pretty much Bicycle...
@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyBestScore(
    scores: List<BestUserScore>,
    onRefresh: () -> Unit,
    onLoadNext: () -> Unit,
    isLoadMoreFlow: StateFlow<Boolean>,
) {
    val isRefreshing by remember {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()
    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)
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
        LazyColumn(state = listState, modifier = Modifier.pullRefresh(state)) {
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
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = state
        )
    }
}