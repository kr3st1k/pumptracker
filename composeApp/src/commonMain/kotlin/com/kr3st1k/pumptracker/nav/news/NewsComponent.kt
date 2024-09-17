package com.kr3st1k.pumptracker.nav.news

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnStart
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.news.News
import com.kr3st1k.pumptracker.core.network.data.news.NewsBanner
import com.kr3st1k.pumptracker.nav.helper.IScrollToUp
import com.kr3st1k.pumptracker.nav.helper.IUpdateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, IUpdateList, IScrollToUp {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    val newsBanners = MutableStateFlow<MutableList<NewsBanner>>(mutableListOf())
    val news = MutableStateFlow<MutableList<News>>(mutableListOf())
    val isRefreshing = MutableStateFlow(false)

    init {
        lifecycle.doOnStart {
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

    override val isScrollable = MutableValue(false)

    override fun scrollUp() {
        isScrollable.value = isScrollable.value.not()
    }

    override fun refreshFun() {
        loadNews()
    }
}