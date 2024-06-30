package dev.kr3st1k.piucompanion.ui.components.home.avatars

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.kr3st1k.piucompanion.core.network.data.AvatarItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyAvatar(
    avatars: List<AvatarItem>,
    onRefresh: () -> Unit,
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
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
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
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (item != null) {
                    Box(
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        item()
                    }
                }
            }
            items(avatars.filter { it.isBought }) { data ->
                AvatarCard(data)
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (avatars.isNotEmpty())
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 8.dp),
                        thickness = 2.dp,
                        color = Color(0xFF222933)
                    )
            }
            items(avatars.filter { !it.isBought }) { data ->
                AvatarCard(data)
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
            if (avatars.isNotEmpty())
                PullToRefreshDefaults.Indicator(state = state, isRefreshing = isRefreshing)
        }
    }
}