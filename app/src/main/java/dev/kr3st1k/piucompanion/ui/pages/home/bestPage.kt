package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.kr3st1k.piucompanion.core.modules.BgManager
import dev.kr3st1k.piucompanion.core.viewmodels.BestUserViewModel
import dev.kr3st1k.piucompanion.ui.components.DropdownMenuBestScores
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.LazyBestScore
import org.koin.core.Koin
import org.koin.core.context.GlobalContext.get

@Composable
fun BestUserPage(
    viewModel: BestUserViewModel,
    listState: LazyGridState,
)
{
    val koin: Koin = get()

    koin.get<BgManager>().checkAndSaveNewUpdatedFiles()

    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val options by viewModel.options.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    LazyBestScore(
        scores,
        onRefresh = { viewModel.loadScores() },
        dropDownMenu = {
            DropdownMenuBestScores(
                options,
                selectedOption,
                onUpdate = {
//                        viewModel.refreshScores(it)
                }
            )
        },
        isRefreshing = isRefreshing,
        listState = listState
    )
    if (scores.isEmpty()) {
        YouSpinMeRightRoundBabyRightRound(
            "Getting best scores... ${viewModel.nowPage.intValue}/${viewModel.pageCount.intValue}",
            progress = (viewModel.nowPage.intValue.toFloat() / viewModel.pageCount.intValue)
        )
    }

}