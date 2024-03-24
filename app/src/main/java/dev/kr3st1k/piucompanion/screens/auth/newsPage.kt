package dev.kr3st1k.piucompanion.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.NewsBanner
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject
import dev.kr3st1k.piucompanion.screens.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.screens.components.home.news.LazyNews
import dev.kr3st1k.piucompanion.screens.components.home.news.NewsSlider
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun NewsScreen(navController: NavController)
{
    val scope = rememberCoroutineScope()
    val newsBanners = remember { mutableStateOf<MutableList<NewsBanner>>(mutableListOf()) }
    val news = remember { mutableStateOf<MutableList<NewsThumbnailObject>>(mutableListOf()) }
    scope.launch {
        newsBanners.value = RequestHandler.getNewsBanners()
        news.value = RequestHandler.getNewsList()
    }
    Column (
        modifier = Modifier.fillMaxSize()
    ){

        if (newsBanners.value.isNotEmpty()) {
            NewsSlider(newsBanners = newsBanners.value);
        }
        else{
            YouSpinMeRightRoundBabyRightRound("Getting news banners...")
        }
        if (newsBanners.value.isNotEmpty() && news.value.isNotEmpty())
            Spacer(modifier = Modifier.height(8.dp))
        if (news.value.isNotEmpty())
        {
            LazyNews(news = news.value, onRefresh = {
                scope.launch {
                    news.value = mutableListOf()
                    news.value = RequestHandler.getNewsList()
                }
            })
        }
        else
        {
            YouSpinMeRightRoundBabyRightRound("Getting news...")
        }
    }
}