package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.db.data.score.LatestScore
import dev.kr3st1k.piucompanion.core.db.repository.ScoresRepository
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.helpers.Utils.convertDateToLocalDateTime
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl.getLatestScores
import dev.kr3st1k.piucompanion.di.DbManager
import dev.kr3st1k.piucompanion.di.InternetManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    val scores = MutableStateFlow<List<LatestScore>>(mutableListOf())
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
            scores.value = scoresRepository.getLatestScores()
            scores.value =
                scores.value.sortedBy { convertDateToLocalDateTime(it.datetime) }.reversed()
        }
    }
}