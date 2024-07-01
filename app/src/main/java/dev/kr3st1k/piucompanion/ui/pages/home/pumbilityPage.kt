package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.PumbilityScore
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.LazyLatestScore
import dev.kr3st1k.piucompanion.ui.components.home.users.UserCard
import dev.kr3st1k.piucompanion.ui.pages.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun PumbilityScreen(
    navController: NavController,
    viewModel: PumbilityViewModel,
    listState: LazyGridState,
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

            }
        )
        if (scores?.isEmpty() == true) {
            YouSpinMeRightRoundBabyRightRound("Getting pumbility scores...")
        }
    }
}


class PumbilityViewModel : ViewModel() {
    val scores = MutableStateFlow<List<PumbilityScore>?>(mutableListOf())
    val user: MutableState<User?> = mutableStateOf(null)

    init {
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            scores.value = mutableListOf()
            val data = NetworkRepositoryImpl.getPumbilityInfo()
            if (data == null) {
                scores.value = null
            } else {
                scores.value = data.scores
                user.value = data.user
            }
        }
    }
}