package dev.kr3st1k.piucompanion.screens.components.home.news

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject

@Composable
fun NewsThumbnail(news: NewsThumbnailObject)
{
    val context = LocalContext.current
    Card(modifier = Modifier
        .fillMaxWidth().
        padding(start = 30.dp, end = 30.dp, top = 14.dp).
        height(90.dp)
        .clickable {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(context, Uri.parse(news.link))
        }
        ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = news.name,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Center)
        }
    }


}