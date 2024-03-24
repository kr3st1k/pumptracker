package dev.kr3st1k.piucompanion.screens.components.home.scores

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.BestUserScore
import dev.kr3st1k.piucompanion.objects.BgInfo
import dev.kr3st1k.piucompanion.objects.LatestScore
import dev.kr3st1k.piucompanion.objects.readBgJson
import dev.kr3st1k.piucompanion.screens.components.YouSpinMeRightRoundBabyRightRound
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyLatestScoreMini(scores: MutableList<LatestScore>, onRefresh: () -> Unit) {
    val isRefreshing by remember {
        mutableStateOf(false)
    }

    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)
    Box (
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(modifier = Modifier.pullRefresh(state)) {
            items(scores.toList()) { data ->
                MiniScore(data)
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = state
        )
    }
}

@SuppressLint("MutableCollectionMutableState", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyBestScoreMini(scores: Pair<MutableList<BestUserScore>, Boolean>, onRefresh: () -> Unit) {
    val isRefreshing by remember {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()
    val pages = remember {
        mutableIntStateOf(2)
    }
    val scope = rememberCoroutineScope()
    val pref = PreferencesManager(LocalContext.current)
    val bgs = remember {
        mutableStateOf<MutableList<BgInfo>>(mutableListOf())
    }

    val context = LocalContext.current
    val scoresNormal = remember {
        mutableStateOf(scores.first.toList())
    }
    val loadMore = remember {
        mutableStateOf(true)
    }
    bgs.value = readBgJson(context)
    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh =
    {
        onRefresh()
        scope.launch {
            scoresNormal.value = mutableListOf()
            val additionalScores = RequestHandler.getBestUserScores(
                pref.getData("cookies", ""),
                pref.getData("ua", ""),
                bgs = bgs.value
            )
            loadMore.value = additionalScores.second
            scoresNormal.value = additionalScores.first.toList()
        }
    })
    Box (
        contentAlignment = Alignment.TopCenter,
    ) {
        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
                .collect { lastVisibleItem ->
                    lastVisibleItem?.let {
                        if (it.index == listState.layoutInfo.totalItemsCount - 5) {
                            if (loadMore.value) {
                                val additionalScores = RequestHandler.getBestUserScores(
                                    pref.getData("cookies", ""),
                                    pref.getData("ua", ""),
                                    bgs = bgs.value,
                                    page = pages.intValue
                                )
                                scoresNormal.value += additionalScores.first.toList()
                                if (additionalScores.second)
                                    pages.intValue += 1
                                else
                                    loadMore.value = false
                            }
                        }
                    }
                }
        }
        if (scoresNormal.value.isNotEmpty()) {
            LazyColumn(state = listState, modifier = Modifier.pullRefresh(state)) {
                items(scoresNormal.value) { data ->
                    MiniBestScore(data)
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = state
            )
        } else {
            YouSpinMeRightRoundBabyRightRound("Getting best scores...")
        }
    }
}
