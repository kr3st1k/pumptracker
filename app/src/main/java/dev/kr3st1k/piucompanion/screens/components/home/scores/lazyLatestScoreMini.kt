package dev.kr3st1k.piucompanion.screens.components.home.scores

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import dev.kr3st1k.piucompanion.objects.LatestScore

@Composable
fun LazyLatestScoreMini(scores: MutableList<LatestScore>) {
    LazyColumn() {
        items(scores.toList()) { data ->
            MiniScore(data)
        }
    }
}