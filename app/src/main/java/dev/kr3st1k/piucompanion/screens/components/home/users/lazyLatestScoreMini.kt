package dev.kr3st1k.piucompanion.screens.components.home.users

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.kr3st1k.piucompanion.objects.LatestScore
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject
import dev.kr3st1k.piucompanion.screens.components.home.news.NewsThumbnail

@Composable
fun LazyLatestScoreMini(scores: MutableList<LatestScore>) {
    LazyColumn() {
        items(scores.toList()) { data ->
            MiniScore(data)
//            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}