package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.viewmodels.UserViewModel
import dev.kr3st1k.piucompanion.di.InternetManager
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.users.UserCard
import dev.kr3st1k.piucompanion.ui.pages.Screen
import dev.kr3st1k.piucompanion.ui.pages.homeDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController,
    viewModel: UserViewModel,
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    val state = rememberPullToRefreshState()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val dests =
        if (InternetManager().hasInternetStatus()) homeDestinations else homeDestinations.filter { it.availableAtOffline }

    if (user == null)
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                state = state,
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.getUserInfo() }
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
                        navController.navigate(it.route)
                    }
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        PullToRefreshDefaults.Indicator(state = state, isRefreshing = isRefreshing)
    }
}