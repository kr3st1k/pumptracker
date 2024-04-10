package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.network.RequestHandler
import dev.kr3st1k.piucompanion.core.network.data.BestUserScore
import dev.kr3st1k.piucompanion.core.network.data.BgInfo
import dev.kr3st1k.piucompanion.core.prefs.BgManager
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.best.DropdownMenuBestScores
import dev.kr3st1k.piucompanion.ui.components.home.scores.best.LazyBestScore
import dev.kr3st1k.piucompanion.ui.screens.Screen
import kotlinx.coroutines.launch

@Composable
fun BestUserPage(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
)
{
    BgManager().checkAndSaveNewUpdatedFiles()

    val viewModel = viewModel<BestUserViewModel>(
        factory = BestUserViewModelFactory { BgManager().readBgJson() }
    )
    val isRecent =
        Utils.rememberLiveData(liveData = viewModel.isRecent, lifecycleOwner, initialValue = false)
    val scores =
        Utils.rememberLiveData(
            liveData = viewModel.scores,
            lifecycleOwner,
            initialValue = mutableListOf()
        )
    val selectedOption = Utils.rememberLiveData(
        liveData = viewModel.selectedOption,
        lifecycleOwner,
        initialValue = Pair("All", "")
    )

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropdownMenuBestScores(
            viewModel.options,
            selectedOption.value,
            onUpdate = { viewModel.refreshScores(it) })

        if (scores.value == null) {
            navController.navigate(Screen.AuthLoadingPage.route) {
                popUpTo(navController.graph.id)
                {
                    inclusive = true
                }
            }
        } else {
            LazyBestScore(
                scores.value!!,
                onRefresh = { viewModel.loadScores() },
                onLoadNext = { viewModel.addScores() },
                isRecent = isRecent
            )
            if (scores.value!!.isEmpty()) {
                YouSpinMeRightRoundBabyRightRound("Getting best scores...")
            }
        }
    }
}


class BestUserViewModel(
    private val bgs: () -> MutableList<BgInfo>,
) : ViewModel() {

    private var _bgs = MutableLiveData(bgs())

    private val _addingScores = MutableLiveData(false)

    private var _pages = MutableLiveData(3)

    private var _selectedOption = MutableLiveData(Pair("All", ""))
    val selectedOption: LiveData<Pair<String, String>> = _selectedOption

    val options = mutableListOf<Pair<String, String>>()
        .apply {
            add("All" to "")
            add("LEVEL 10 OVER" to "10over")
            for (i in 10..27)
                add("LEVEL $i" to i.toString())
            add("LEVEL 27 OVER" to "27over")
            add("CO-OP" to "coop")
        }

    val scores = MutableLiveData<List<BestUserScore>?>(mutableListOf())

    private var _isRecent = MutableLiveData(false)
    var isRecent: LiveData<Boolean> = _isRecent

    init {
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            scores.value = mutableListOf()
            _isRecent.value = false
            _bgs = MutableLiveData(bgs())
            val newScores = RequestHandler.getBestUserScores(
                lvl = selectedOption.value!!.second,
                bgs = _bgs.value!!
            )
            if (newScores != null) {
                scores.value = newScores.first.toList()
                _isRecent.value = newScores.second
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
        if (_addingScores.value == false) {
            viewModelScope.launch {
                _bgs = MutableLiveData(bgs())
                _addingScores.value = true
                val additionalScores = RequestHandler.getBestUserScores(
                    page = _pages.value,
                    lvl = _selectedOption.value!!.second,
                    bgs = _bgs.value!!
                )
                if (additionalScores != null) {
                    scores.value = scores.value?.plus(additionalScores.first.toList())
                    _isRecent.value = additionalScores.second
                    _pages.value = _pages.value?.plus(1)
                    _addingScores.value = false
                } else {
                    scores.value = null
                }
            }
        }
    }

}

class BestUserViewModelFactory(
    private val bgsFunc: () -> MutableList<BgInfo>,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BestUserViewModel::class.java)) {
            return BestUserViewModel(bgsFunc) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}