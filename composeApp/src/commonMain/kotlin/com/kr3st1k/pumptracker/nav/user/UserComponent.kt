package com.kr3st1k.pumptracker.nav.user

import com.arkivanov.decompose.ComponentContext
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.di.LoginManager
import com.kr3st1k.pumptracker.nav.RootComponent
import com.kr3st1k.pumptracker.nav.currentPage
import com.kr3st1k.pumptracker.nav.navigateUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserComponent(
    val navigateTo: (RootComponent.TopLevelConfiguration) -> Unit,
    val navigateToLogin: () -> Unit,
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

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
                if (tmp == null) {
                    currentPage = null
                    navigateUp = null
                    navigateToLogin()
                }
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