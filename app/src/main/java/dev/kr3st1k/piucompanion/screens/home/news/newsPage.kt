package dev.kr3st1k.piucompanion.screens.home.news

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.components.home.news.LazyNews
import dev.kr3st1k.piucompanion.components.home.news.NewsSlider
import dev.kr3st1k.piucompanion.objects.NewsBanner
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NewsScreen(navController: NavController, lifecycleOwner: LifecycleOwner) {
    val viewModel = viewModel<NewsViewModel>()

    var newsBanners by remember { mutableStateOf<MutableList<NewsBanner>?>(null) }
    var news by remember { mutableStateOf<MutableList<NewsThumbnailObject>?>(null) }

    val newsBannersObserver = Observer<MutableList<NewsBanner>> { newBanners ->
        newsBanners = newBanners
    }

    val newsObserver = Observer<MutableList<NewsThumbnailObject>> { newNews ->
        news = newNews
    }

    viewModel.newsBanners.observe(lifecycleOwner, newsBannersObserver)
    viewModel.news.observe(lifecycleOwner, newsObserver)

    Column(modifier = Modifier.fillMaxSize()) {
        if (newsBanners.isNullOrEmpty()) {
            YouSpinMeRightRoundBabyRightRound("Getting news banners...")
        } else {
            NewsSlider(newsBanners = newsBanners!!)
        }

        if (newsBanners != null && news != null && news!!.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (news.isNullOrEmpty()) {
            YouSpinMeRightRoundBabyRightRound("Getting news...")
        } else {
            LazyNews(
                news = news!!,
                onRefresh = { viewModel.refreshNews() }
            )
        }
    }
}