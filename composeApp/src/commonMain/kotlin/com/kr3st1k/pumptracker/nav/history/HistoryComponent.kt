package com.kr3st1k.pumptracker.nav.history

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.kr3st1k.pumptracker.core.db.data.score.LatestScore
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.helpers.Utils
import com.kr3st1k.pumptracker.core.helpers.Utils.convertDateToLocalDateTime
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl.getLatestScores
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.nav.RootComponent
import com.kr3st1k.pumptracker.nav.currentPage
import com.kr3st1k.pumptracker.nav.navigateUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class HistoryComponent(
    val navigateTo: (RootComponent.TopLevelConfiguration) -> Unit,
    val navigateToLogin: () -> Unit,
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
    val scores = mutableStateListOf<LatestScore>()
    private val scoresRepository: ScoresRepository = ScoresRepository(DbManager().getScoreDao())
    val isRefreshing = mutableStateOf(false)

    init {
        loadScores()
        fetchAndAddToDb()
    }

    fun fetchAndAddToDb() {
        isRefreshing.value = true
        if (InternetManager().hasInternetStatus())
            viewModelScope.launch {
                val tmp = getLatestScores(50)
                if (tmp == null) {
                    currentPage = null
                    navigateUp = null
                    navigateToLogin()
                }
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