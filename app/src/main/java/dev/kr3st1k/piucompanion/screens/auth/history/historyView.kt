package dev.kr3st1k.piucompanion.screens.auth.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.LatestScore
import kotlinx.coroutines.launch

class HistoryViewModel(private val pref: PreferencesManager) : ViewModel() {
    private val _checkLogin = MutableLiveData(false)
    val checkLogin: LiveData<Boolean> = _checkLogin

    private val _checkingLogin = MutableLiveData(true)
    val checkingLogin: LiveData<Boolean> = _checkingLogin

    val scores = MutableLiveData<MutableList<LatestScore>>(mutableListOf())

    init {
        loadScores()
    }

    private fun loadScores() {
        viewModelScope.launch {
            _checkLogin.value = RequestHandler.checkIfLoginSuccess(
                pref.getData("cookies", ""),
                pref.getData("ua", "")
            )
            _checkingLogin.value = false
            if (checkLogin.value == true) {
                scores.value = RequestHandler.getLatestScores(
                    pref.getData("cookies", ""),
                    pref.getData("ua", ""),
                    50
                )
            }
        }
    }

    fun refreshScores() {
        viewModelScope.launch {
            scores.value = mutableListOf()
            scores.value = RequestHandler.getLatestScores(
                pref.getData("cookies", ""),
                pref.getData("ua", ""),
                50
            )
        }
    }
}

class HistoryViewModelFactory(private val pref: PreferencesManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}