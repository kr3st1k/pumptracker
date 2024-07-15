package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.db.data.score.BestScore
import dev.kr3st1k.piucompanion.core.db.repository.ScoresRepository
import dev.kr3st1k.piucompanion.core.helpers.Utils.getNewBestScoresFromWeb
import dev.kr3st1k.piucompanion.di.DbManager
import dev.kr3st1k.piucompanion.di.InternetManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BestUserViewModel : ViewModel() {

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

}