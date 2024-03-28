package dev.kr3st1k.piucompanion.screens.home.scores.best

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.BestUserScore
import dev.kr3st1k.piucompanion.objects.BgInfo
import kotlinx.coroutines.launch

class BestUserViewModel(
    private val pref: PreferencesManager,
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
            _checkLogin.value = RequestHandler.checkIfLoginSuccess()
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
    private val pref: PreferencesManager,
    private val bgsFunc: () -> MutableList<BgInfo>,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BestUserViewModel::class.java)) {
            return BestUserViewModel(pref, bgsFunc) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}