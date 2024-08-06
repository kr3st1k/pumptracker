package com.kr3st1k.pumptracker.core.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kr3st1k.pumptracker.core.db.data.score.LatestScore
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.helpers.Utils
import com.kr3st1k.pumptracker.core.helpers.Utils.convertDateToLocalDateTime
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl.getLatestScores
import com.kr3st1k.pumptracker.di.BgManager
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.InternetManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    val scores = mutableStateListOf<LatestScore>()
    //val scores = MutableStateFlow<List<LatestScore>>(listOf())
    private val scoresRepository: ScoresRepository = ScoresRepository(DbManager().getScoreDao())
    val isRefreshing = mutableStateOf(false)
    val needAuth = mutableStateOf(false)

    init {
        loadScores()
        fetchAndAddToDb()
    }

    fun fetchAndAddToDb() {
        isRefreshing.value = true
        if (InternetManager().hasInternetStatus())
            viewModelScope.launch {
                val tmp = getLatestScores(50)
                if (tmp == null)
                    needAuth.value = true
                scoresRepository.insertLatestScores(
                    tmp!!.map {
                        LatestScore(
                            songName = it.songName,
                            songBackgroundUri = it.songBackgroundUri,
                            difficulty = it.difficulty,
                            score = it.score,
                            rank = it.rank,
                            datetime = it.datetime,
                            hash = Utils.generateHashForScore(
                                it.score,
                                it.difficulty,
                                it.songName,
                                it.datetime
                            )
                        )
                    }
                )
                loadScores()
                isRefreshing.value = false
            }
        else
            isRefreshing.value = false
    }

    private fun loadScores() {
        viewModelScope.launch {
            scores.clear()
            scores.addAll(
                scoresRepository.getLatestScores()
                .sortedBy { convertDateToLocalDateTime(it.datetime) }
                .reversed()
            )
        }
    }
}