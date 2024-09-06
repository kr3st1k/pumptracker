package com.kr3st1k.pumptracker.nav.best

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnResume
import com.kr3st1k.pumptracker.core.db.data.score.BestScore
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.helpers.Utils.getNewBestScoresFromWeb
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.nav.RootComponent
import com.kr3st1k.pumptracker.nav.helper.IScrollToUp
import com.kr3st1k.pumptracker.nav.helper.IUpdateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class BestUserComponent(
    val navigateTo: (RootComponent.TopLevelConfiguration) -> Unit,
    val navigateToLogin: () -> Unit,
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent, IScrollToUp, IUpdateList {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
    private val scoresRepository: ScoresRepository = ScoresRepository(DbManager().getScoreDao())
    val isLoaded = mutableStateOf(false)
    val pageCount = mutableIntStateOf(1)
    val nowPage = mutableIntStateOf(1)
    val isRefreshing = MutableStateFlow(false)
    val selectedOption = mutableStateOf(Pair("All", 0))
    val needAuth = mutableStateOf(false)

    val options =
        mutableListOf<Pair<String, Int>>().apply {
            add("All" to 0)
//            add("LEVEL 10 OVER" to "10over")
            for (i in 10..28)
                add("LEVEL $i" to i)
//            add("LEVEL 27 OVER" to "27over")
//            add("CO-OP" to "coop")
        }


    val scores = MutableStateFlow<List<BestScore>>(mutableListOf())


    init {
        lifecycle.doOnResume {
            loadScores()
            fetchAndAddToDb()
        }
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


                    navigateToLogin()
                    return@launch
                }

                scoresRepository.insertBestScores(scoresTmp.reversed())

                loadScores()
                isRefreshing.value = false
                isLoaded.value = true
            }
        else {
            viewModelScope.launch {
                loadScores()
                isRefreshing.value = false
            }
        }
    }

    private fun loadScores() {
        viewModelScope.launch {
            scores.value = scoresRepository.getBestScores()
            scores.value = scores.value.reversed()
            if (selectedOption.value.second != 0)
                scores.value = scores.value.filter {
                    it.difficulty.filter { it2 -> it2.isDigit() }
                        .map { it2 -> it2.toString().toInt() }.joinToString("")
                        .toInt() == selectedOption.value.second
                }
        }
    }

    fun refreshScores(value: Pair<String, Int>) {
        selectedOption.value = value
        loadScores()
    }

    override val isScrollable = MutableValue(false)

    override fun scrollUp() {
        isScrollable.value = isScrollable.value.not()
    }

    override fun refreshFun() {
        fetchAndAddToDb()
    }
}