package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.db.dao.ScoresDao
import dev.kr3st1k.piucompanion.core.db.data.BestScore
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.modules.DbManager
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BestUserViewModel : ViewModel() {

    private val db = DbManager()
    private val scoresDao: ScoresDao = db.getScoreDao()
    val pageCount = mutableIntStateOf(1)
    val nowPage = mutableIntStateOf(1)
    val isRefreshing = MutableStateFlow(false)
    private var _selectedOption = MutableStateFlow(Pair("All", ""))
    val selectedOption: StateFlow<Pair<String, String>> = _selectedOption

    val options = MutableStateFlow(
        mutableListOf<Pair<String, String>>().apply {
            add("All" to "")
            add("LEVEL 10 OVER" to "10over")
            for (i in 10..27)
                add("LEVEL $i" to i.toString())
            add("LEVEL 27 OVER" to "27over")
            add("CO-OP" to "coop")
        }
    )


    val scores = MutableStateFlow<List<BestScore>>(mutableListOf())

    init {
        loadScores()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchAndAddToDb() {
        viewModelScope.launch {
            isRefreshing.value = true
            nowPage.intValue = 1
            pageCount.intValue = 1
            var isInside = false
            var tmp = NetworkRepositoryImpl.getBestUserScores(page = nowPage.intValue)
            pageCount.intValue = tmp!!.lastPageNumber
            while (tmp?.isLoadMore == true && !isInside) {
                for (it in tmp.res) {
                    val score = BestScore(
                        songName = it.songName,
                        difficulty = it.difficulty,
                        score = it.score,
                        rank = it.rank,
                        hash = Utils.generateHashForScore(
                            "0",
                            it.difficulty,
                            it.songName
                        )
                    )

                    if (scores.value.contains(score)) {
                        isInside = true
                        break
                    }

                    GlobalScope.async {
                        scoresDao.insertBest(
                            score
                        )
                    }.await()
                }
                nowPage.intValue += 1
                tmp = NetworkRepositoryImpl.getBestUserScores(page = nowPage.intValue)
            }
            scores.value = GlobalScope.async { scoresDao.getAllBestScores() }.await()
            isRefreshing.value = false
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadScores() {
        viewModelScope.launch {
            scores.value = GlobalScope.async { scoresDao.getAllBestScores() }.await()
            fetchAndAddToDb()
//            val newScores = NetworkRepositoryImpl.getBestUserScores(
//                lvl = selectedOption.value.second
//            )
//            if (newScores != null) {
//                scores.value = newScores.res.toList()
//                _isLoadMore.value = newScores.isLoadMore
//                _pages.value = 3
//            } else {
//                scores.value = null
//            }
        }
    }

//    fun refreshScores(selectedOption: Pair<String, String>) {
//        viewModelScope.launch {
//            _selectedOption.value = selectedOption
//            loadScores()
//        }
//    }
//
//    fun addScores() {
//        if (!_addingScores.value) {
//            viewModelScope.launch {
//                _addingScores.value = true
//                val additionalScores = NetworkRepositoryImpl.getBestUserScores(
//                    page = _pages.value,
//                    lvl = _selectedOption.value.second
//                )
//                if (additionalScores != null) {
//                    scores.value = scores.value?.plus(additionalScores.res.toList())
//                    _isLoadMore.value = additionalScores.isLoadMore
//                    _pages.value = _pages.value.plus(1)
//                    _addingScores.value = false
//                } else {
//                    scores.value = null
//                }
//            }
//        }
//    }

}