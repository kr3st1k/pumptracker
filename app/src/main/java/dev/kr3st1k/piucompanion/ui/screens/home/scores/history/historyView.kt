package dev.kr3st1k.piucompanion.ui.screens.home.scores.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.LatestScore
import kotlinx.coroutines.launch

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
            _checkLogin.value = RequestHandler.checkIfLoginSuccess()
            _checkingLogin.value = false
            if (checkLogin.value == true) {
                scores.value = RequestHandler.getLatestScores(
                    50
                )
            }
        }
    }
}
