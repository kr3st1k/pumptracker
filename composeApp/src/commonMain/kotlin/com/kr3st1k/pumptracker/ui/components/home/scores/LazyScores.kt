package com.kr3st1k.pumptracker.ui.components.home.scores

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kr3st1k.pumptracker.core.db.data.score.BestScore
import com.kr3st1k.pumptracker.core.db.data.score.Score
import com.kr3st1k.pumptracker.di.BgManager
import com.kr3st1k.pumptracker.getPlatform
import com.kr3st1k.pumptracker.ScrollBarForDesktop
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpeenMeRightRoundBabyRightRoundTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyScores(
    scores: List<Score>,
    onRefresh: () -> Unit,
    dropDownMenu: @Composable (() -> Unit)? = null,
    listState: LazyGridState,
    item: @Composable (() -> Unit)? = null,
    isRefreshing: Boolean,
) {
    val state = rememberPullToRefreshState()
    val bgs = BgManager().readBgJson()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(370.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
            modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .pullToRefresh(
                        enabled = getPlatform().type != "Desktop",
                        state = state,
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                    )
           
        ) {
            if (dropDownMenu != null)
                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    dropDownMenu()
                }
            if (item != null)
                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    item()
                }
            if (scores.isNotEmpty()) {
                items(scores.take(50)) { data ->
                    if (data.songBackgroundUri == null) {
                        data.songBackgroundUri =
                            bgs.find { tt -> tt.song_name == data.songName }?.jacket
                                ?: "https://www.piugame.com/l_img/bg1.png"
                    }
                    ScoreCard(data)
                }
                if (scores.count() > 50) {
                    if (scores.first() is BestScore && item != null)
                        item(span = {
                            GridItemSpan(maxLineSpan)
                        }) {
                            HorizontalDivider(
                                modifier = Modifier.padding(bottom = 4.dp),
                                thickness = 2.dp,
                                color = Color(0xFF222933)
                            )
                        }
                    items(scores.subList(51, scores.count())) { data ->
                        if (data.songBackgroundUri == null) {
                            data.songBackgroundUri =
                                bgs.find { tt -> tt.song_name == data.songName }?.jacket
                                    ?: "https://www.piugame.com/l_img/bg1.png"
                        }
                        ScoreCard(data)

                    }
                }
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