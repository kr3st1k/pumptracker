package dev.kr3st1k.piucompanion.screens.home.scores.best

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

    var bgs = readBgJson(context)

    private val _isFirstTime = MutableLiveData(true)

    private val _checkLogin = MutableLiveData(false)
    val checkLogin: LiveData<Boolean> = _checkLogin

    private val _checkingLogin = MutableLiveData(true)
    val checkingLogin: LiveData<Boolean> = _checkingLogin

    private val _addingScores = MutableLiveData(false)

    var _pages = MutableLiveData(3)

    var _selectedOption = MutableLiveData(Pair("All", ""))
    val selectedOption: LiveData<Pair<String, String>> = _selectedOption

    val options = listOf(
        Pair("All", ""),
        Pair("LEVEL 10", "10"),
        Pair("LEVEL 11", "11"),
        Pair("LEVEL 12", "12"),
        Pair("LEVEL 13", "13"),
        Pair("LEVEL 14", "14"),
        Pair("LEVEL 15", "15"),
        Pair("LEVEL 16", "16"),
        Pair("LEVEL 17", "17"),
        Pair("LEVEL 18", "18"),
        Pair("LEVEL 19", "19"),
        Pair("LEVEL 20", "20"),
        Pair("LEVEL 21", "21"),
        Pair("LEVEL 22", "22"),
        Pair("LEVEL 23", "23"),
        Pair("LEVEL 24", "24"),
        Pair("LEVEL 25", "25"),
        Pair("LEVEL 26", "26"),
        Pair("LEVEL 27 OVER", "27over"),
        Pair("LEVEL 10 OVER", "10over"),
        Pair("CO-OP", "coop")
    )

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
            _checkLogin.value = RequestHandler.checkIfLoginSuccess(
                pref.getData("cookies", ""),
                pref.getData("ua", "")
            )
            if (_isFirstTime.value == true) {
                _checkingLogin.value = false
                _isFirstTime.value = false
            }
            if (checkLogin.value == true) {
                bgs = readBgJson(context)
                val newScores = RequestHandler.getBestUserScores(
                    pref.getData("cookies", ""),
                    pref.getData("ua", ""),
                    bgs = bgs,
                    lvl = selectedOption.value!!.second
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
                _addingScores.value = true
                val additionalScores = RequestHandler.getBestUserScores(
                    pref.getData("cookies", ""),
                    pref.getData("ua", ""),
                    bgs = bgs,
                    lvl = _selectedOption.value!!.second,
                    page = _pages.value
                )
                scores.value = scores.value?.plus(additionalScores.first.toList())
                _isRecent.value = additionalScores.second
                _pages.value = _pages.value?.plus(1)
                _addingScores.value = false
            }
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