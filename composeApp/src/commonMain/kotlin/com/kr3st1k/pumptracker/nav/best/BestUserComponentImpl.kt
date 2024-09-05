package com.kr3st1k.pumptracker.nav.best

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kr3st1k.pumptracker.di.BgManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.nav.refreshFunction
import com.kr3st1k.pumptracker.ui.components.home.DropdownMenuBestScores
import com.kr3st1k.pumptracker.ui.components.home.scores.LazyScores
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@Composable
fun BestUserComponentImpl(viewModel: BestUserComponent, listState: LazyGridState) {
    BgManager().checkAndSaveNewUpdatedFiles()

    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    refreshFunction.value = { viewModel.fetchAndAddToDb() }

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