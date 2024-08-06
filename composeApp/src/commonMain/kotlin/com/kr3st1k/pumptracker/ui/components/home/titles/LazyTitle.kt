package com.kr3st1k.pumptracker.ui.components.home.titles

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.kr3st1k.pumptracker.core.db.data.title.PhoenixCategoryItem
import com.kr3st1k.pumptracker.core.network.data.title.TitleItem
import com.kr3st1k.pumptracker.getPlatform
import com.kr3st1k.pumptracker.ScrollBarForDesktop
import kotlinx.coroutines.launch
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpeenMeRightRoundBabyRightRoundTop

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
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.background)
                .pullToRefresh(
                    enabled = getPlatform().type != "Desktop",
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
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
                            .padding(vertical = 4.dp)
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
        YouSpeenMeRightRoundBabyRightRoundTop(state, isRefreshing)
        if (getPlatform().type == "Desktop") {
            ScrollBarForDesktop(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
                    .fillMaxHeight(),
                listState
            )
        }
    }
}