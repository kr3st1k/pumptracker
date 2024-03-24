package dev.kr3st1k.piucompanion.screens.auth.best

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.BestUserScore
import dev.kr3st1k.piucompanion.objects.readBgJson
import kotlinx.coroutines.launch

class BestUserViewModel(private val context: Context) : ViewModel() {

    val pref = PreferencesManager(context)

    private val _checkLogin = MutableLiveData(false)
    val checkLogin: LiveData<Boolean> = _checkLogin

    private val _checkingLogin = MutableLiveData(true)
    val checkingLogin: LiveData<Boolean> = _checkingLogin

    val scores =
        MutableLiveData<Pair<MutableList<BestUserScore>, Boolean>>(Pair(mutableListOf(), false))

    init {
        loadScores()
    }

    private fun loadScores() {
        viewModelScope.launch {
            _checkingLogin.value = true
            val bgs = readBgJson(context)
            val newScores = RequestHandler.getBestUserScores(
                pref.getData("cookies", ""),
                pref.getData("ua", ""),
                bgs = bgs
            )
            scores.value = newScores
            _checkLogin.value = pref.getData("cookies", "") != ""
            _checkingLogin.value = false
        }
    }
}

class BestUserViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BestUserViewModel::class.java)) {
            return BestUserViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}