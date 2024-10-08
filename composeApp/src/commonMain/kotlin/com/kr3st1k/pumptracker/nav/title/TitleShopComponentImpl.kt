package com.kr3st1k.pumptracker.nav.title

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.kr3st1k.pumptracker.ui.components.home.titles.LazyTitle
import com.kr3st1k.pumptracker.ui.components.home.users.UserCard
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@Composable
fun TitleShopComponentImpl(viewModel: TitleShopComponent) {
    val titles by viewModel.titles.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()
    val tappedState by viewModel.isScrollable.subscribeAsState()

    LaunchedEffect(tappedState) {
        listState.animateScrollToItem(0)
    }


    LazyTitle(
        titles = titles!!,
        onRefresh = { viewModel.loadTitles() },
        item = {
            Column(
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.user.value != null) {
                    UserCard(viewModel.user.value!!, true)
                } else {
                    YouSpinMeRightRoundBabyRightRound()
                }
            }
            Spacer(modifier = Modifier.size(4.dp))

        },
        listState = listState,
        isRefreshing = isRefreshing,
        onSetTitle = { value: String -> viewModel.setAvatar(value) }
    )


    if (titles?.isEmpty() == true) {
        YouSpinMeRightRoundBabyRightRound("Getting titles...")
    }

}