package com.kr3st1k.pumptracker.ui.components.home.news

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.kr3st1k.pumptracker.core.network.data.news.NewsBanner
import com.kr3st1k.pumptracker.openSiteInBrowser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsSlider(newsBanners: MutableList<NewsBanner>) {
    val pagerState = rememberCarouselState(
        initialItem = 0,
        itemCount = { newsBanners.size }
    )
    HorizontalMultiBrowseCarousel(
        modifier = Modifier.padding(horizontal = 16.dp),
        state = pagerState,
        preferredItemWidth = 400.dp,
        itemSpacing = 10.dp,
//        contentPadding = PaddingValues(horizontal = 16.dp),
    ) { i ->
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = ButtonDefaults.outlinedButtonBorder(false),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(Color.Transparent),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(0.dp),
//                modifier = Modifier.width(420.dp),
                onClick = {
                    openSiteInBrowser(newsBanners[i].uri)
                }
            ) {
                SubcomposeAsyncImage(
                    model = newsBanners[i].pictureUrl,
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
//    HorizontalMultiBrowseCarousel(
////        snapPosition = SnapPosition.Center,
//        contentPadding = PaddingValues(horizontal = 16.dp),
//        pageSize = PageSize.Fill,
//        key = null,
//        pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
//            pagerState, Orientation.Horizontal
//        ),
}