package dev.kr3st1k.piucompanion.screens.components.home.news

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject

@Composable
fun LazyNews(news: MutableList<NewsThumbnailObject>) {
    LazyColumn() {
        items(news.toList()) { data ->
            NewsThumbnail(news = data)
        }
    }
}
