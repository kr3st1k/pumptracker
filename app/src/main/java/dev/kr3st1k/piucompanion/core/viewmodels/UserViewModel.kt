package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.db.repository.ScoresRepository
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.di.DbManager
import dev.kr3st1k.piucompanion.di.InternetManager
import dev.kr3st1k.piucompanion.di.LoginManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    val user = MutableStateFlow<User?>(User())
    private val scoresRepository = ScoresRepository(DbManager().getScoreDao())
    val isRefreshing = MutableStateFlow(false)

    init {
        getUserInfo()
    }

    fun getUserInfo() {
        viewModelScope.launch {
            isRefreshing.value = true
            user.value = LoginManager().getUserData()
            if (InternetManager().hasInternetStatus()) {
                val tmp = NetworkRepositoryImpl.getUserInfo()
                user.value = tmp
            }
            val scores =
                scoresRepository.getBestScores()
            if (scores.isNotEmpty()) {
                var pumbility = 0
                scores.filter { it.difficulty[0] != 'C' }.sortedBy { it.pumbilityScore }
                    .reversed().take(50).forEach { pumbility += it.pumbilityScore }
                user.value?.pumbility = pumbility.toString()
            }
            LoginManager().saveUserData(user.value!!)
            isRefreshing.value = false
        }
    }
}