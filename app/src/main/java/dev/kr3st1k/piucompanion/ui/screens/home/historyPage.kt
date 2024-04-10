package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.network.RequestHandler
import dev.kr3st1k.piucompanion.core.network.data.LatestScore
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.latest.LazyLatestScore
import dev.kr3st1k.piucompanion.ui.screens.Screen
import kotlinx.coroutines.launch

@Composable
fun HistoryPage(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
) {
    val viewModel = viewModel<HistoryViewModel>()
    val scores =
        Utils.rememberLiveData(viewModel.scores, lifecycleOwner, initialValue = mutableListOf())

    Column(modifier = Modifier.fillMaxSize()) {

        if (scores.value == null) {
            navController.navigate(Screen.AuthLoadingPage.route) {
                popUpTo(navController.graph.id)
                {
                    inclusive = true
                }
            }
        } else {
            LazyLatestScore(scores.value!!, onRefresh = { viewModel.loadScores() })
            if (scores.value?.isEmpty() == true) {
                YouSpinMeRightRoundBabyRightRound("Getting latest scores...")
            }
        }
    }
}


class HistoryViewModel : ViewModel() {
    val scores = MutableLiveData<MutableList<LatestScore>?>(mutableListOf())

    init {
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            scores.value = mutableListOf()
            scores.value = RequestHandler.getLatestScores(
                50
            )
        }
    }
}