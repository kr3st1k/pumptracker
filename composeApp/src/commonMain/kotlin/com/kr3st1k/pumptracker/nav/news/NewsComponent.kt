package com.kr3st1k.pumptracker.nav.news

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.news.News
import com.kr3st1k.pumptracker.core.network.data.news.NewsBanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    val newsBanners = MutableStateFlow<MutableList<NewsBanner>>(mutableListOf())
    val news = MutableStateFlow<MutableList<News>>(mutableListOf())
    val isRefreshing = MutableStateFlow(false)

    init {
        lifecycle.doOnResume {
            loadNews()
        }
    }

    private fun loadNews() {
        viewModelScope.launch {
            newsBanners.value = NetworkRepositoryImpl.getNewsBanners()
            news.value = NetworkRepositoryImpl.getNewsList()
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            isRefreshing.value = true
            news.value = NetworkRepositoryImpl.getNewsList()
            isRefreshing.value = false
        }
    }
}