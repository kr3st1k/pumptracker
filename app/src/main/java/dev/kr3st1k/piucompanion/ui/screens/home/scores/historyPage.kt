package dev.kr3st1k.piucompanion.ui.screens.home.scores

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.helpers.RequestHandler
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.objects.LatestScore
import dev.kr3st1k.piucompanion.ui.components.MyAlertDialog
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.scores.latest.LazyLatestScore
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun HistoryPage(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
) {
    val viewModel = viewModel<HistoryViewModel>()

    val checkLogin =
        Utils.rememberLiveData(viewModel.checkLogin, lifecycleOwner, initialValue = false)
    val checkingLogin =
        Utils.rememberLiveData(viewModel.checkingLogin, lifecycleOwner, initialValue = true)
    val scores = Utils.rememberLiveData(viewModel.scores, lifecycleOwner, initialValue = null)

    Column(modifier = Modifier.fillMaxSize()) {
        if (checkingLogin.value) {
            YouSpinMeRightRoundBabyRightRound("Check if you logged in...")
        } else {
            if (checkLogin.value) {
                if (scores.value.isNotEmpty()) {
                    LazyLatestScore(scores.value, onRefresh = { viewModel.loadScores() })
                } else {
                    YouSpinMeRightRoundBabyRightRound("Getting latest scores...")
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

class HistoryViewModel : ViewModel() {
    private val _checkLogin = MutableLiveData(false)
    val checkLogin: LiveData<Boolean> = _checkLogin

    private val _checkingLogin = MutableLiveData(true)
    val checkingLogin: LiveData<Boolean> = _checkingLogin

    val scores = MutableLiveData<MutableList<LatestScore>>(mutableListOf())

    init {
        loadScores()
    }

    fun loadScores() {
        viewModelScope.launch {
            _checkingLogin.value = true
            scores.value = mutableListOf()
            _checkLogin.value = RequestHandler.checkIfLoginSuccessRequest()
            _checkingLogin.value = false
            if (checkLogin.value == true) {
                scores.value = RequestHandler.getLatestScores(
                    50
                )
            }
        }
    }
}