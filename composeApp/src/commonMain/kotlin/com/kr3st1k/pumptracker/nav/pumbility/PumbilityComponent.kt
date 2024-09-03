package com.kr3st1k.pumptracker.nav.pumbility

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.kr3st1k.pumptracker.core.db.data.score.BestScore
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.helpers.Utils.getNewBestScoresFromWeb
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.di.LoginManager
import com.kr3st1k.pumptracker.nav.RootComponent
import com.kr3st1k.pumptracker.nav.currentPage
import com.kr3st1k.pumptracker.nav.navigateUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class PumbilityComponent(
    val navigateTo: (RootComponent.TopLevelConfiguration) -> Unit,
    val navigateToLogin: () -> Unit,
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

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
                if (needAuth.value) {
                    currentPage = null
                    navigateUp = null
                    navigateToLogin()
                    return@launch
                }
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
                if (tmp == null) {
                    currentPage = null
                    navigateUp = null
                    navigateToLogin()
                    return@launch
                }
                user.value = tmp
            }
            user.value?.pumbility = pumbility.toString()
            LoginManager().saveUserData(user.value!!)
        }
    }
}