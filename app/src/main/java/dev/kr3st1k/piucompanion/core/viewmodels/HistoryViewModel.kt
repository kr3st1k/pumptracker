package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.db.dao.ScoresDao
import dev.kr3st1k.piucompanion.core.db.data.LatestScore
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.helpers.Utils.convertDateToLocalDateTime
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl.getLatestScores
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.di.DbManager
import dev.kr3st1k.piucompanion.di.InternetManager
import dev.kr3st1k.piucompanion.di.LoginManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    val scores = MutableStateFlow<List<LatestScore>>(mutableListOf())
    private val db = DbManager()
    private val scoresDao: ScoresDao = db.getScoreDao()
    val isRefreshing = MutableStateFlow(false)
    val needAuth = mutableStateOf(false)

    init {
        loadScores()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchAndAddToDb() {
        isRefreshing.value = true
        if (InternetManager().hasInternetStatus())
            viewModelScope.launch {
            val tmp = getLatestScores(50)
                if (tmp == null)
                    needAuth.value = true
            tmp?.forEach {
                GlobalScope.async {
                    scoresDao.insertLatest(
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
                    )
                }.await()
            }
            scores.value = GlobalScope.async { scoresDao.getAllLatestScores() }.await()
                scores.value =
                    scores.value.sortedBy { convertDateToLocalDateTime(it.datetime) }.reversed()
            isRefreshing.value = false
        }
        else
            isRefreshing.value = false
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadScores() {
        viewModelScope.launch {
            scores.value = GlobalScope.async { scoresDao.getAllLatestScores() }.await()
            scores.value =
                scores.value.sortedBy { convertDateToLocalDateTime(it.datetime) }.reversed()
            var tmpUser: User? = LoginManager().getUserData()
            if (InternetManager().hasInternetStatus()) {
                tmpUser = NetworkRepositoryImpl.getUserInfo()
                if (tmpUser != null)
                    LoginManager().saveUserData(tmpUser)
                else
                    needAuth.value = true
            }
            fetchAndAddToDb()
        }
    }
}