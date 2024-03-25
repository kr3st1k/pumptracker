package dev.kr3st1k.piucompanion.screens.home.news

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.components.Utils
import dev.kr3st1k.piucompanion.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.components.home.news.LazyNews
import dev.kr3st1k.piucompanion.components.home.news.NewsSlider

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NewsScreen(navController: NavController, lifecycleOwner: LifecycleOwner) {
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

        if (news.value.isNullOrEmpty()) {
            YouSpinMeRightRoundBabyRightRound("Getting news...")
        } else {
            LazyNews(
                news = news.value,
                onRefresh = { viewModel.refreshNews() }
            )
        }
    }
}