package com.kr3st1k.pumptracker.nav.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kr3st1k.pumptracker.ScrollBarForDesktop
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.getPlatform
import com.kr3st1k.pumptracker.nav.homeDestinations
import com.kr3st1k.pumptracker.nav.refreshFunction
import com.kr3st1k.pumptracker.ui.components.home.users.UserCard
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpeenMeRightRoundBabyRightRoundTop
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserComponentImpl(viewModel: UserComponent) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    val state = rememberPullToRefreshState()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val dests =
        if (InternetManager().hasInternetStatus()) homeDestinations else homeDestinations.filter { it.availableAtOffline }

    refreshFunction.value = { viewModel.getUserInfo() }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .pullToRefresh(
                    enabled = getPlatform().type != "Desktop",
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = { viewModel.getUserInfo() },
                )
        ) {
            item {
                Column(
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (user?.trueUser == true) {
                        UserCard(user!!)
                    } else {
                        YouSpinMeRightRoundBabyRightRound()
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
            items(dests) {
                ListItem(
                    headlineContent = {
                        Text(text = it.iconText)
                    },
                    supportingContent = {
                        if (it.summary != null)
                            Text(text = it.summary)
                    },
                    leadingContent = {
                        Icon(
                            imageVector = it.selectedIcon,
                            contentDescription = it.iconText
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.navigateTo(it.route)
                        }
                )
            }
        }
        YouSpeenMeRightRoundBabyRightRoundTop(state, isRefreshing)
        if (getPlatform().type == "Desktop") {
            ScrollBarForDesktop(
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
                    .fillMaxHeight(),
                listState
            )
        }
    }
}