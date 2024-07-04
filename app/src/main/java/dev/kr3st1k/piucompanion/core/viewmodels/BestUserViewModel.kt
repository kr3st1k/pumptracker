package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.db.dao.ScoresDao
import dev.kr3st1k.piucompanion.core.db.data.BestScore
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.di.DbManager
import dev.kr3st1k.piucompanion.di.InternetManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BestUserViewModel : ViewModel() {

    private val db = DbManager()
    private val scoresDao: ScoresDao = db.getScoreDao()
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
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchAndAddToDb() {
        isRefreshing.value = true
        if (InternetManager().hasInternetStatus())
            viewModelScope.launch {
                isRefreshing.value = true
                isLoaded.value = false
                nowPage.intValue = 1
                pageCount.intValue = 1
                var isInside = false
                var tmp = NetworkRepositoryImpl.getBestUserScores(page = nowPage.intValue)
                if (tmp == null)
                    needAuth.value = true
                pageCount.intValue = tmp!!.lastPageNumber
                val scoresTmp = mutableListOf<BestScore>()
                while (nowPage.intValue != pageCount.intValue + 1 && !isInside) {
                    for (it in tmp!!.res) {
                        val score = BestScore(
                            songName = it.songName,
                            difficulty = it.difficulty,
                            score = it.score,
                            rank = it.rank,
                            hash = Utils.generateHashForScore(
                                "0",
                                it.difficulty,
                                it.songName
                            ),
                            pumbilityScore = Utils.getPoints(it.difficulty, it.score)
                        )

                        if (scores.value.contains(score)) {
                            isInside = true
                            break
                        }

                        scoresTmp.add(score)
                    }
                    nowPage.intValue += 1
                    tmp = NetworkRepositoryImpl.getBestUserScores(page = nowPage.intValue)
                }

                for (value in scoresTmp.reversed())
                    GlobalScope.async {
                        scoresDao.insertBest(
                            value
                        )
                    }.await()

                scores.value = GlobalScope.async { scoresDao.getAllBestScores() }.await()
                isRefreshing.value = false
                isLoaded.value = true
            }
        else {
            viewModelScope.launch {
                scores.value = GlobalScope.async { scoresDao.getAllBestScores() }.await()
                isRefreshing.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadScores() {
        viewModelScope.launch {
            scores.value = GlobalScope.async { scoresDao.getAllBestScores() }.await()
            if (selectedOption.value.second != 0)
                scores.value = scores.value.filter {
                    it.difficulty.filter { it2 -> it2.isDigit() }
                        .map { it2 -> it2.toString().toInt() }.joinToString("")
                        .toInt() == selectedOption.value.second
                }
            fetchAndAddToDb()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun refreshScores(value: Pair<String, Int>) {
        viewModelScope.launch {
            selectedOption.value = value
            scores.value = GlobalScope.async { scoresDao.getAllBestScores() }.await()
            if (value.second != 0)
                scores.value = scores.value.filter {
                    it.difficulty.filter { it2 -> it2.isDigit() }
                        .map { it2 -> it2.toString().toInt() }.joinToString("")
                        .toInt() == value.second
                }

        }
    }

}