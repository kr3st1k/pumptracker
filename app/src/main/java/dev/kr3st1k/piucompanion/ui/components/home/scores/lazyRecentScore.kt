package dev.kr3st1k.piucompanion.ui.components.home.scores

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.kr3st1k.piucompanion.core.network.data.Score
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyLatestScore(
    scores: List<Score>,
    onRefresh: () -> Unit,
    listState: LazyListState,
    item: @Composable() (() -> Unit)? = null,
) {
    var isRefreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val state = rememberPullToRefreshState()

    val scaleFraction = {
        if (isRefreshing) 0f
        else LinearOutSlowInEasing.transform(state.distanceFraction).coerceIn(0f, 1f)
    }
    Box(
        contentAlignment = Alignment.TopCenter
    )
    {
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
                if (item != null)
                    item()
            }
            items(scores) { data ->
                ScoreCard(data)
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

