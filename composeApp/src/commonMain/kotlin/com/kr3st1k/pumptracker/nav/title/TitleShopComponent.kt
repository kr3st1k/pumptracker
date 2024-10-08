package com.kr3st1k.pumptracker.nav.title

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnStart
import com.kr3st1k.pumptracker.core.db.data.title.PhoenixTitle
import com.kr3st1k.pumptracker.core.db.data.title.PhoenixTitleList
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.core.network.data.title.TitleItem
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.nav.helper.IScrollToUp
import com.kr3st1k.pumptracker.nav.helper.IUpdateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TitleShopComponent(
    val navigateToLogin: () -> Unit,
    componentContext: ComponentContext
) : ComponentContext by componentContext, IUpdateList, IScrollToUp {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    val titles = MutableStateFlow<List<TitleItem>?>(mutableListOf())
    val user: MutableState<User?> = mutableStateOf(null)
    val isRefreshing = MutableStateFlow(false)
    private val scoresRepository = ScoresRepository(DbManager().getScoreDao())

    init {
        lifecycle.doOnStart {
            loadTitles()
        }
    }

    fun loadTitles() {
        viewModelScope.launch {
            isRefreshing.value = true
            val data = NetworkRepositoryImpl.getTitleShopInfo()
            if (data == null) {


                navigateToLogin()
            } else {
                val scores =
                    scoresRepository.getBestScores()
                data.titles.forEach { title ->
                    val titleInfo = PhoenixTitleList.titles.find { it.name == title.name }
                    if (titleInfo == null)
                        title.titleInfo = PhoenixTitle(name = title.name)
                    else {
                        title.titleInfo = titleInfo
                        if (!title.isAchieved && scores.isNotEmpty()) {
                            title.progress = titleInfo.completionProgress(scores)
                            title.progressValue = titleInfo.completionProgressValue(scores)
                        }
                    }
                }

                titles.value = data.titles
                user.value = data.user
            }
            isRefreshing.value = false
        }
    }

    suspend fun setAvatar(value: String) {
        isRefreshing.value = true
        NetworkRepositoryImpl.setTitle(value)
        loadTitles()
    }

    override val isScrollable = MutableValue(false)

    override fun scrollUp() {
        isScrollable.value = isScrollable.value.not()
    }

    override fun refreshFun() {
        loadTitles()
    }
}