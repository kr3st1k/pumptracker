package com.kr3st1k.pumptracker.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.di.LoginManager
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