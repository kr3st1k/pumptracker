package dev.kr3st1k.piucompanion.ui.components.home.scores

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyBestScore(
    scores: List<BestUserScore>,
    onRefresh: () -> Unit,
    onLoadNext: () -> Unit,
    isLoadMoreFlow: StateFlow<Boolean>,
    dropDownMenu: @Composable () -> Unit,
    listState: LazyListState,
) {
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val state = rememberPullToRefreshState()
    val isLoadMore by isLoadMoreFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val scaleFraction = {
        if (isRefreshing) 0f
        else LinearOutSlowInEasing.transform(state.distanceFraction).coerceIn(0f, 1f)
    }

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
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pullToRefresh(
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        scope.launch {
                            state.animateToHidden()
                            isRefreshing = true
                            onRefresh()
                            isRefreshing = false
                        }
                    }
                )
        ) {
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
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    scaleX = scaleFraction()
                    scaleY = scaleFraction()
                }
        ) {
            if (scores.isNotEmpty())
                PullToRefreshDefaults.Indicator(state = state, isRefreshing = isRefreshing)
        }
    }
}
