package dev.kr3st1k.piucompanion.ui.pages.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.viewmodels.BestUserViewModel
import dev.kr3st1k.piucompanion.di.BgManager
import dev.kr3st1k.piucompanion.di.InternetManager
import dev.kr3st1k.piucompanion.ui.components.DropdownMenuBestScores
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.LazyScores
import dev.kr3st1k.piucompanion.ui.pages.Screen

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BestUserPage(
    navController: NavController,
    viewModel: BestUserViewModel,
    listState: LazyGridState,
) {
    BgManager().checkAndSaveNewUpdatedFiles()

    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    if (viewModel.needAuth.value)
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }

    LazyScores(
        scores,
        onRefresh = { viewModel.fetchAndAddToDb() },
        dropDownMenu = {
            DropdownMenuBestScores(
                viewModel.options,
                viewModel.selectedOption.value,
                onUpdate = {
                    viewModel.refreshScores(it)
                }
            )
        },
        isRefreshing = isRefreshing,
        listState = listState
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