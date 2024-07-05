package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.db.dao.ScoresDao
import dev.kr3st1k.piucompanion.core.db.data.score.BestScore
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.di.DbManager
import dev.kr3st1k.piucompanion.di.InternetManager
import dev.kr3st1k.piucompanion.di.LoginManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PumbilityViewModel : ViewModel() {
    val scores = MutableStateFlow<List<BestScore>>(mutableListOf())
    val user: MutableState<User?> = mutableStateOf(null)
    private val db = DbManager()
    val isLoaded = mutableStateOf(false)
    val pageCount = mutableIntStateOf(1)
    val nowPage = mutableIntStateOf(1)
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
                isRefreshing.value = true
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
                scores.value = scores.value.sortedBy { it.pumbilityScore }.reversed()
                user.value = LoginManager().getUserData()
                if (InternetManager().hasInternetStatus()) {
                    val tmpUser = NetworkRepositoryImpl.getUserInfo()
                    user.value = tmpUser
                    if (tmpUser != null)
                        LoginManager().saveUserData(tmpUser)
                }
                isRefreshing.value = false
                isLoaded.value = true
            }
        else {
            viewModelScope.launch {
                scores.value = GlobalScope.async { scoresDao.getAllBestScores() }.await()
                scores.value = scores.value.sortedBy { it.pumbilityScore }.reversed()
                user.value = LoginManager().getUserData()
                isRefreshing.value = false
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadScores() {
        viewModelScope.launch {
            scores.value = GlobalScope.async { scoresDao.getAllBestScores() }.await()
            scores.value = scores.value.sortedBy { it.pumbilityScore }.reversed()
            user.value = LoginManager().getUserData()
            if (InternetManager().hasInternetStatus()) {
                val tmp = NetworkRepositoryImpl.getUserInfo()
                user.value = tmp
                if (tmp != null)
                    LoginManager().saveUserData(tmp)
            }
            fetchAndAddToDb()
        }
    }
}