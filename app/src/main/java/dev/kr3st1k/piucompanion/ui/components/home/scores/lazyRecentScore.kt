package dev.kr3st1k.piucompanion.ui.components.home.scores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.kr3st1k.piucompanion.core.db.data.Score

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyLatestScore(
    scores: List<Score>,
    onRefresh: () -> Unit,
    listState: LazyGridState,
    item: @Composable() (() -> Unit)? = null,
    isRefreshing: Boolean,
) {

    val state = rememberPullToRefreshState()

    Box(
        contentAlignment = Alignment.TopCenter
    )
    {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(370.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pullToRefresh(
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh
                )
        ) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
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
        ) {
            if (scores.isNotEmpty())
                PullToRefreshDefaults.Indicator(state = state, isRefreshing = isRefreshing)
        }
    }

}

