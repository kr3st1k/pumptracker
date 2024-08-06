package com.kr3st1k.pumptracker.core.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kr3st1k.pumptracker.core.db.data.score.BestScore
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.helpers.Utils.getNewBestScoresFromWeb
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.di.LoginManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PumbilityViewModel : ViewModel() {
    val scores = MutableStateFlow<List<BestScore>>(mutableListOf())
    val user: MutableState<User?> = mutableStateOf(null)
    val isLoaded = mutableStateOf(false)
    val pageCount = mutableIntStateOf(1)
    val nowPage = mutableIntStateOf(1)
    private val scoresRepository: ScoresRepository = ScoresRepository(DbManager().getScoreDao())
    val isRefreshing = MutableStateFlow(false)
    val needAuth = mutableStateOf(false)

    init {
        loadScores()
        fetchAndAddToDb()
    }

    fun fetchAndAddToDb() {
        isRefreshing.value = true
        if (InternetManager().hasInternetStatus())
            viewModelScope.launch {
                isRefreshing.value = true
                isLoaded.value = false
                nowPage.intValue = 1
                pageCount.intValue = 1
                val scoresTmp = getNewBestScoresFromWeb(
                    nowPage, pageCount, needAuth, scoresRepository
                )
                scoresRepository.insertBestScores(scoresTmp.reversed())

                loadScores()
                isRefreshing.value = false
                isLoaded.value = true
            }
        else {
            loadScores()
                isRefreshing.value = false

        }
    }

    private fun loadScores() {
        viewModelScope.launch {
            var pumbility = 0
            scores.value = scoresRepository.getBestScores()
            scores.value = scores.value.sortedBy { it.pumbilityScore }.reversed()
            scores.value.filter { it.difficulty[0] != 'C' }.take(50)
                .forEach { pumbility += it.pumbilityScore }
            user.value = LoginManager().getUserData()
            if (InternetManager().hasInternetStatus()) {
                val tmp = NetworkRepositoryImpl.getUserInfo()
                user.value = tmp
            }
            user.value?.pumbility = pumbility.toString()
            LoginManager().saveUserData(user.value!!)
        }
    }
}