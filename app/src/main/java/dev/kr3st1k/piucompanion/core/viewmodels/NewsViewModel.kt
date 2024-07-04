package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    val newsBanners = MutableStateFlow<MutableList<NewsBanner>>(mutableListOf())
    val news = MutableStateFlow<MutableList<News>>(mutableListOf())
    val isRefreshing = MutableStateFlow(false)

    init {
        loadNews()
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