package dev.kr3st1k.piucompanion.screens.home.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.NewsBanner
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    val newsBanners = MutableLiveData<MutableList<NewsBanner>>(mutableListOf())
    val news = MutableLiveData<MutableList<NewsThumbnailObject>>(mutableListOf())

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            newsBanners.value = RequestHandler.getNewsBanners()
            news.value = RequestHandler.getNewsList()
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            news.value = mutableListOf()
            news.value = RequestHandler.getNewsList()
        }
    }
}
