package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.viewmodels.PumbilityViewModel
import dev.kr3st1k.piucompanion.di.InternetManager
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.LazyLatestScore
import dev.kr3st1k.piucompanion.ui.components.home.users.UserCard
import dev.kr3st1k.piucompanion.ui.pages.Screen

@Composable
fun PumbilityScreen(
    navController: NavController,
    viewModel: PumbilityViewModel,
    listState: LazyGridState,
) {
    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    if (viewModel.needAuth.value)
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }

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

        },
        isRefreshing = isRefreshing
    )
    if (scores.isEmpty()) {
        if (InternetManager().hasInternetStatus()) {
            if (!viewModel.isLoaded.value)
                YouSpinMeRightRoundBabyRightRound(
                    "Getting best scores... ${viewModel.nowPage.intValue}/${viewModel.pageCount.intValue}",
                    progress = (viewModel.nowPage.intValue.toFloat() / viewModel.pageCount.intValue)
                )
            else
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Scores")
                }
        } else
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No information.\nPlease reopen app when will be internet")
            }
    }
}