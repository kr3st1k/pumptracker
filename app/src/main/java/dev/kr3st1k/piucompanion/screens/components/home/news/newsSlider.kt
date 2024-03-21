package dev.kr3st1k.piucompanion.screens.components.home.news

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import dev.kr3st1k.piucompanion.objects.NewsBanner

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewsSlider(newsBanners: MutableList<NewsBanner>) {
    val pagerState = rememberPagerState(initialPage = 0)
    val context = LocalContext.current
    HorizontalPager(
        pageCount = newsBanners.size, state = pagerState,
        contentPadding = PaddingValues(horizontal = 32.dp),
        pageSpacing = 16.dp,
        verticalAlignment = Alignment.CenterVertically,
    ) { page ->
        Card(
            colors = CardDefaults.cardColors(Color.Transparent),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(0.dp),
            onClick = {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(context, Uri.parse(newsBanners[page].uri))
            }
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(newsBanners[page].pictureUrl)
                    .memoryCacheKey(newsBanners[page].pictureUrl)
                    .diskCacheKey(newsBanners[page].pictureUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(130.dp)
            )
        }

    }
}