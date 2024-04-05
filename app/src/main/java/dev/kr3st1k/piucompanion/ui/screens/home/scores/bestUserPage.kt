package dev.kr3st1k.piucompanion.ui.screens.home.scores

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.helpers.RequestHandler
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.objects.BestUserScore
import dev.kr3st1k.piucompanion.core.objects.BgInfo
import dev.kr3st1k.piucompanion.core.objects.checkAndSaveNewUpdatedFiles
import dev.kr3st1k.piucompanion.core.objects.readBgJson
import dev.kr3st1k.piucompanion.ui.components.MyAlertDialog
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.best.DropdownMenuBestScores
import dev.kr3st1k.piucompanion.ui.components.home.scores.best.LazyBestScore
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState", "CoroutineCreationDuringComposition")
@Composable
fun BestUserPage(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
)
{
    val context = LocalContext.current

    checkAndSaveNewUpdatedFiles(context)

    val viewModel = viewModel<BestUserViewModel>(
        factory = BestUserViewModelFactory { readBgJson(context) }
    )

    val checkingLogin = Utils.rememberLiveData(
        liveData = viewModel.checkingLogin,
        lifecycleOwner,
        initialValue = true
    )
    val checkLogin = Utils.rememberLiveData(
        liveData = viewModel.checkLogin,
        lifecycleOwner,
        initialValue = false
    )
    val isRecent =
        Utils.rememberLiveData(liveData = viewModel.isRecent, lifecycleOwner, initialValue = false)
    val scores =
        Utils.rememberLiveData(liveData = viewModel.scores, lifecycleOwner, initialValue = null)
    val selectedOption = Utils.rememberLiveData(
        liveData = viewModel.selectedOption,
        lifecycleOwner,
        initialValue = Pair("All", "")
    )

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        DropdownMenuBestScores(
            viewModel.options,
            selectedOption.value,
            onUpdate = { viewModel.refreshScores(it) })
        if (checkingLogin.value) {
            YouSpinMeRightRoundBabyRightRound("Check if you logged in...")
        } else {
            if (checkLogin.value) {
                if (scores.value.isNotEmpty()) {
                    LazyBestScore(
                        scores.value,
                        onRefresh = { viewModel.loadScores() },
                        onLoadNext = { viewModel.addScores() },
                        isRecent = isRecent.value
                    )
                }
                else
                {
                    YouSpinMeRightRoundBabyRightRound("Getting best scores...")
                }
            } else {
                MyAlertDialog(
                    showDialog = true,
                    title = "Login failed!",
                    content = "You need to authorize",
                    onDismiss = {}
                )
            }
        }
    }
}

class BestUserViewModel(
    private val bgs: () -> MutableList<BgInfo>,
) : ViewModel() {

    private val _isFirstTime = MutableLiveData(true)

    private var _bgs = MutableLiveData(bgs())

    private val _checkLogin = MutableLiveData(false)
    val checkLogin: LiveData<Boolean> = _checkLogin

    private val _checkingLogin = MutableLiveData(true)
    val checkingLogin: LiveData<Boolean> = _checkingLogin

    private val _addingScores = MutableLiveData(false)

    var _pages = MutableLiveData(3)

    var _selectedOption = MutableLiveData(Pair("All", ""))
    val selectedOption: LiveData<Pair<String, String>> = _selectedOption

    val options = mutableListOf<Pair<String, String>>()
        .apply {
            add("All" to "")
            for (i in 10..27)
                add("LEVEL $i" to i.toString())
            add("LEVEL 27 OVER" to "27over")
            add("CO-OP" to "coop")
        }

    val scores = MutableLiveData<List<BestUserScore>>(mutableListOf())

    var _isRecent = MutableLiveData(false)
    var isRecent: LiveData<Boolean> = _isRecent

    init {
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            scores.value = mutableListOf()
            _isRecent.value = false
            if (_isFirstTime.value == true)
                _checkingLogin.value = true
            _checkLogin.value = RequestHandler.checkIfLoginSuccessRequest()
            if (_isFirstTime.value == true) {
                _checkingLogin.value = false
                _isFirstTime.value = false
            }
            if (checkLogin.value == true) {
                _bgs = MutableLiveData(bgs())
                val newScores = RequestHandler.getBestUserScores(
                    lvl = selectedOption.value!!.second,
                    bgs = _bgs.value!!
                )
                scores.value = newScores.first.toList()
                _pages.value = 3
                _isRecent.value = newScores.second
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
                scores.value = scores.value?.plus(additionalScores.first.toList())
                _isRecent.value = additionalScores.second
                _pages.value = _pages.value?.plus(1)
                _addingScores.value = false
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