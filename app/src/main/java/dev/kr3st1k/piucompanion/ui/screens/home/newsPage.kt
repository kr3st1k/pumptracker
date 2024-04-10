package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.network.RequestHandler
import dev.kr3st1k.piucompanion.core.network.data.News
import dev.kr3st1k.piucompanion.core.network.data.NewsBanner
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.news.LazyNews
import dev.kr3st1k.piucompanion.ui.components.home.news.NewsSlider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun NewsScreen(
    viewModel: NewsViewModel,
) {
    val newsBanners by viewModel.newsBanners.collectAsStateWithLifecycle()
    val news = viewModel.news.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        if (newsBanners.isEmpty()) {
            YouSpinMeRightRoundBabyRightRound("Getting news banners...")
        } else {
            NewsSlider(newsBanners = newsBanners)
        }

        if (news.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyNews(
            news = news.value,
            onRefresh = { viewModel.refreshNews() }
        )
        if (news.value.isEmpty()) {
            YouSpinMeRightRoundBabyRightRound("Getting news...")
        }
    }
}

class NewsViewModel : ViewModel() {
    val newsBanners = MutableStateFlow<MutableList<NewsBanner>>(mutableListOf())
    val news = MutableStateFlow<MutableList<News>>(mutableListOf())

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