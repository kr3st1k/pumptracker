package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.LatestScore
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.LazyLatestScore
import dev.kr3st1k.piucompanion.ui.screens.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun HistoryPage(
    navController: NavController,
    viewModel: HistoryViewModel,
    listState: LazyListState,
) {
    val scores by viewModel.scores.collectAsStateWithLifecycle()



    if (scores == null) {
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }
    } else {
        LazyLatestScore(
            scores!!,
            onRefresh = { viewModel.loadScores() },
            listState = listState
        )
        if (scores?.isEmpty() == true) {
            YouSpinMeRightRoundBabyRightRound("Getting latest scores...")
        }
    }

}


class HistoryViewModel : ViewModel() {
    val scores = MutableStateFlow<MutableList<LatestScore>?>(mutableListOf())

    init {
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            scores.value = mutableListOf()
            scores.value = NetworkRepositoryImpl.getLatestScores(50)
        }
    }
}