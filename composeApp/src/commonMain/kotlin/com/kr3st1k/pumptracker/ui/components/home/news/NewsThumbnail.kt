package com.kr3st1k.pumptracker.ui.components.home.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kr3st1k.pumptracker.core.network.data.news.News
import com.kr3st1k.pumptracker.openSiteInBrowser

@Composable
fun NewsThumbnail(news: News) {
    val colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 14.dp)
        .padding(horizontal = 16.dp)
        .clickable {
            openSiteInBrowser(news.link)
        },
        colors = colors,
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = news.name,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}