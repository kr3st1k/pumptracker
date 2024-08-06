package com.kr3st1k.pumptracker.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.news.News
import com.kr3st1k.pumptracker.core.network.data.news.NewsBanner
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