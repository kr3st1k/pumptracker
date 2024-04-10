package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.network.RequestHandler
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.news.LazyNews
import dev.kr3st1k.piucompanion.ui.components.home.news.NewsSlider
import kotlinx.coroutines.launch

@Composable
fun NewsScreen(lifecycleOwner: LifecycleOwner) {
    val viewModel = viewModel<NewsViewModel>()
    val newsBanners = Utils.rememberLiveData(viewModel.newsBanners, lifecycleOwner, null)
    val news = Utils.rememberLiveData(viewModel.news, lifecycleOwner, null)

    Column(modifier = Modifier.fillMaxSize()) {
        if (newsBanners.value.isNullOrEmpty()) {
            YouSpinMeRightRoundBabyRightRound("Getting news banners...")
        } else {
            NewsSlider(newsBanners = newsBanners.value)
        }

        if (newsBanners.value != null && news.value != null && news.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyNews(
            news = news.value ?: mutableListOf(),
            onRefresh = { viewModel.refreshNews() }
        )
        if (news.value.isNullOrEmpty()) {
            YouSpinMeRightRoundBabyRightRound("Getting news...")
        }
    }
}

class NewsViewModel : ViewModel() {
    val newsBanners = MutableLiveData<MutableList<NewsBanner>>(mutableListOf())
    val news = MutableLiveData<MutableList<News>>(mutableListOf())

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