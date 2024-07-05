package dev.kr3st1k.piucompanion.ui.components.home.titles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.kr3st1k.piucompanion.core.db.data.title.PhoenixCategoryItem
import dev.kr3st1k.piucompanion.core.network.data.title.TitleItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LazyTitle(
    titles: List<TitleItem>,
    onRefresh: () -> Unit,
    item: @Composable() (() -> Unit)? = null,
    listState: LazyGridState,
    isRefreshing: Boolean,
    onSetTitle: suspend (value: String) -> Unit
) {
    val scope = rememberCoroutineScope()

    val state = rememberPullToRefreshState()

    val selectedFilters = remember { mutableStateListOf<PhoenixCategoryItem>() }

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
            modifier = Modifier
                .padding(horizontal = 16.dp)
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
                if (item != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        item()
                    }
                }
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (titles.isNotEmpty())
                    FlowRow(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.Center
                    ) {
                        PhoenixCategoryItem.entries.forEach { category ->
                            FilterChip(
                                selected = selectedFilters.contains(category),
                                onClick = {
                                    if (selectedFilters.contains(category))
                                        selectedFilters.remove(category)
                                    else
                                        selectedFilters.add(category)
                                },
                                label = {
                                    Text(text = category.name)
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
            }
            items(titles.filter { it.isAchieved }
                .filter { if (selectedFilters.isNotEmpty()) selectedFilters.contains(it.titleInfo!!.category) else true }) { data ->
                TitleCard(
                    data,
                    action = {
                        if (!data.isSelected)
                            scope.launch {
                                onSetTitle(data.value)
                            }

                    }
                )
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (titles.filter { it.isAchieved }
                        .filter { if (selectedFilters.isNotEmpty()) selectedFilters.contains(it.titleInfo!!.category) else true }
                        .isNotEmpty())
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 8.dp),
                        thickness = 2.dp,
                        color = Color(0xFF222933)
                    )
            }
            items(titles.filter { it.progress != null }.filter { it.progress != 0F }
                .sortedBy { it.progress }
                .filter { if (selectedFilters.isNotEmpty()) selectedFilters.contains(it.titleInfo!!.category) else true }
                .reversed()) { data ->
                TitleCard(
                    data,
                    action = { }
                )
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (titles.filter { it.progress != null }.filter { it.progress != 0F }
                        .sortedBy { it.progress }
                        .filter { if (selectedFilters.isNotEmpty()) selectedFilters.contains(it.titleInfo!!.category) else true }
                        .isNotEmpty() && titles.filter { !it.isAchieved }
                        .filter { it.progress == 0F || it.progress == null }
                        .filter { if (selectedFilters.isNotEmpty()) selectedFilters.contains(it.titleInfo!!.category) else true }
                        .isNotEmpty())
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 8.dp),
                        thickness = 2.dp,
                        color = Color(0xFF222933)
                    )
            }
            items(titles.filter { !it.isAchieved }
                .filter { it.progress == 0F || it.progress == null }
                .filter { if (selectedFilters.isNotEmpty()) selectedFilters.contains(it.titleInfo!!.category) else true }) { data ->
                TitleCard(
                    data,
                    action = { }
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            if (titles.isNotEmpty())
                PullToRefreshDefaults.Indicator(state = state, isRefreshing = isRefreshing)
        }
    }
}