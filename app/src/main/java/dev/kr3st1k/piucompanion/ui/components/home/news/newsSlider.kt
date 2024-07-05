package dev.kr3st1k.piucompanion.ui.components.home.news

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import dev.kr3st1k.piucompanion.core.network.data.news.NewsBanner

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsSlider(newsBanners: MutableList<NewsBanner>) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        newsBanners.size
    }
    val context = LocalContext.current
    HorizontalPager(
        modifier = Modifier,
        state = pagerState,
        pageSpacing = 16.dp,
        userScrollEnabled = true,
        reverseLayout = false,
        contentPadding = PaddingValues(horizontal = 32.dp),
        pageSize = PageSize.Fill,
        key = null,
        pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
            pagerState, Orientation.Horizontal
        ),
        pageContent = { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            )
            {
                Card(
                    colors = CardDefaults.cardColors(Color.Transparent),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier.width(400.dp),
                    onClick = {
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        customTabsIntent.launchUrl(context, Uri.parse(newsBanners[page].uri))
                    }
                ) {

                    SubcomposeAsyncImage(
                        model = newsBanners[page].pictureUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(130.dp),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            )
                            {
                                CircularProgressIndicator()
                            }
                        }
                    )
                }
            }
        }
    )
}