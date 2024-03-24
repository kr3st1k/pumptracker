package dev.kr3st1k.piucompanion.screens.components.home.scores.best

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.BestUserScore
import dev.kr3st1k.piucompanion.objects.BgInfo
import dev.kr3st1k.piucompanion.objects.readBgJson
import dev.kr3st1k.piucompanion.screens.components.YouSpinMeRightRoundBabyRightRound
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch

// This whole file is pretty much Bicycle...
@SuppressLint("MutableCollectionMutableState", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyBestScore(scores: Pair<MutableList<BestUserScore>, Boolean>) {
    val isRefreshing by remember {
        mutableStateOf(false)
    }
    val listState = rememberLazyListState()
    val pages = remember {
        mutableIntStateOf(3)
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
    val isRecent = remember {
        mutableStateOf(false)
    }
    var selectedOption by remember { mutableStateOf(Pair("All", "")) }

    val options = listOf(
        Pair("All", ""),
        Pair("LEVEL 10", "10"),
        Pair("LEVEL 11", "11"),
        Pair("LEVEL 12", "12"),
        Pair("LEVEL 13", "13"),
        Pair("LEVEL 14", "14"),
        Pair("LEVEL 15", "15"),
        Pair("LEVEL 16", "16"),
        Pair("LEVEL 17", "17"),
        Pair("LEVEL 18", "18"),
        Pair("LEVEL 19", "19"),
        Pair("LEVEL 20", "20"),
        Pair("LEVEL 21", "21"),
        Pair("LEVEL 22", "22"),
        Pair("LEVEL 23", "23"),
        Pair("LEVEL 24", "24"),
        Pair("LEVEL 25", "25"),
        Pair("LEVEL 26", "26"),
        Pair("LEVEL 27 OVER", "27over"),
        Pair("LEVEL 10 OVER", "10over"),
        Pair("CO-OP", "coop")
    )
    var expanded by remember { mutableStateOf(false) }

    bgs.value = readBgJson(context)
    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh =
    {
        scope.launch {
            scoresNormal.value = mutableListOf()
            val additionalScores = RequestHandler.getBestUserScores(
                pref.getData("cookies", ""),
                pref.getData("ua", ""),
                lvl = selectedOption.second,
                bgs = bgs.value
            )
            isRecent.value = additionalScores.second
            pages.intValue = 2
            scoresNormal.value = additionalScores.first.toList()
        }
    })
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier.fillMaxWidth(),
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedOption.first,
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true
            )
            ExposedDropdownMenu(
                expanded = expanded,
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { (name, value) ->
                    DropdownMenuItem(
                        text = {
                            Text(text = name)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            selectedOption = Pair(name, value)
                            scope.launch {
                                scoresNormal.value = mutableListOf()
                                val additionalScores = RequestHandler.getBestUserScores(
                                    pref.getData("cookies", ""),
                                    pref.getData("ua", ""),
                                    lvl = selectedOption.second,
                                    bgs = bgs.value
                                )
                                isRecent.value = additionalScores.second
                                pages.intValue = 2
                                scoresNormal.value = additionalScores.first.toList()
                            }
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    Box(
        contentAlignment = Alignment.TopCenter,
    ) {
        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
                .collect { lastVisibleItem ->
                    lastVisibleItem?.let {
                        if (it.index == listState.layoutInfo.totalItemsCount - 1) {
                            if (!isRecent.value) {
                                val additionalScores = RequestHandler.getBestUserScores(
                                    pref.getData("cookies", ""),
                                    pref.getData("ua", ""),
                                    bgs = bgs.value,
                                    lvl = selectedOption.second,
                                    page = pages.intValue
                                )
                                scoresNormal.value += additionalScores.first.toList()
                                if (!additionalScores.second)
                                    pages.intValue += 1
                                else
                                    isRecent.value = true
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