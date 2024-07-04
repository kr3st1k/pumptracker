package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kr3st1k.piucompanion.core.viewmodels.PumbilityViewModel
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.LazyLatestScore
import dev.kr3st1k.piucompanion.ui.components.home.users.UserCard

@Composable
fun PumbilityScreen(
    viewModel: PumbilityViewModel,
    listState: LazyGridState,
) {
    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    LazyLatestScore(
        scores,
        onRefresh = { viewModel.loadScores() },
        listState = listState,
        item = {
            Column(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.user.value != null) {
                    UserCard(viewModel.user.value!!, true)
                } else {
                    YouSpinMeRightRoundBabyRightRound()
                }
            }
//                Spacer(modifier = Modifier.size(4.dp))

        },
        isRefreshing = isRefreshing
    )
    if (scores.isEmpty()) {
        YouSpinMeRightRoundBabyRightRound("Getting pumbility scores...")
    }
}