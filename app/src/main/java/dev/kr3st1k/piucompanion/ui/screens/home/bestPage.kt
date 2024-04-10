package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.modules.BgManager
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.best.DropdownMenuBestScores
import dev.kr3st1k.piucompanion.ui.components.home.scores.best.LazyBestScore
import dev.kr3st1k.piucompanion.ui.screens.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.core.context.GlobalContext.get

@Composable
fun BestUserPage(
    navController: NavController,
    viewModel: BestUserViewModel,
)
{
    val koin: Koin = get()

    koin.get<BgManager>().checkAndSaveNewUpdatedFiles()

    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val options by viewModel.options.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropdownMenuBestScores(
            options,
            selectedOption,
            onUpdate = { viewModel.refreshScores(it) })

        if (scores == null) {
            navController.navigate(Screen.AuthLoadingPage.route) {
                popUpTo(navController.graph.id)
                {
                    inclusive = true
                }
            }
        } else {
            LazyBestScore(
                scores!!,
                onRefresh = { viewModel.loadScores() },
                onLoadNext = { viewModel.addScores() },
                isLoadMoreFlow = viewModel.isLoadMore
            )
            if (scores!!.isEmpty()) {
                YouSpinMeRightRoundBabyRightRound("Getting best scores...")
            }
        }
    }
}


class BestUserViewModel : ViewModel() {

    private val _addingScores = MutableStateFlow(false)

    private var _pages = MutableStateFlow(3)

    private var _selectedOption = MutableStateFlow(Pair("All", ""))
    val selectedOption: StateFlow<Pair<String, String>> = _selectedOption

    val options = MutableStateFlow(
        mutableListOf<Pair<String, String>>().apply {
            add("All" to "")
            add("LEVEL 10 OVER" to "10over")
            for (i in 10..27)
                add("LEVEL $i" to i.toString())
            add("LEVEL 27 OVER" to "27over")
            add("CO-OP" to "coop")
        }
    )


    val scores = MutableStateFlow<List<BestUserScore>?>(mutableListOf())

    private var _isLoadMore = MutableStateFlow(false)
    var isLoadMore: StateFlow<Boolean> = _isLoadMore

    init {
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            scores.value = mutableListOf()
            _isLoadMore.value = false
            val newScores = NetworkRepositoryImpl.getBestUserScores(
                lvl = selectedOption.value.second
            )
            if (newScores != null) {
                scores.value = newScores.res.toList()
                _isLoadMore.value = newScores.isLoadMore
                _pages.value = 3
            } else {
                scores.value = null
            }
        }
    }

    fun refreshScores(selectedOption: Pair<String, String>) {
        viewModelScope.launch {
            _selectedOption.value = selectedOption
            loadScores()
        }
    }

    fun addScores() {
        if (!_addingScores.value) {
            viewModelScope.launch {
                _addingScores.value = true
                val additionalScores = NetworkRepositoryImpl.getBestUserScores(
                    page = _pages.value,
                    lvl = _selectedOption.value.second
                )
                if (additionalScores != null) {
                    scores.value = scores.value?.plus(additionalScores.res.toList())
                    _isLoadMore.value = additionalScores.isLoadMore
                    _pages.value = _pages.value.plus(1)
                    _addingScores.value = false
                } else {
                    scores.value = null
                }
            }
        }
    }

}