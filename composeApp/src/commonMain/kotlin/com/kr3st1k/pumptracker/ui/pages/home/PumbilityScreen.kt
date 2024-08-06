package com.kr3st1k.pumptracker.ui.pages.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kr3st1k.pumptracker.core.viewmodels.PumbilityViewModel
import com.kr3st1k.pumptracker.di.BgManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound
import com.kr3st1k.pumptracker.ui.components.home.scores.LazyScores
import com.kr3st1k.pumptracker.ui.components.home.users.UserCard
import com.kr3st1k.pumptracker.ui.pages.Screen
import com.kr3st1k.pumptracker.ui.pages.currentPage
import com.kr3st1k.pumptracker.ui.pages.navigateUp
import com.kr3st1k.pumptracker.ui.pages.refreshFunction

@Composable
fun PumbilityScreen(
    navController: NavController,
    listState: LazyGridState,
    viewModel: PumbilityViewModel,
) {
    BgManager().checkAndSaveNewUpdatedFiles()

    val scores by viewModel.scores.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    refreshFunction.value = { viewModel.fetchAndAddToDb() }
    
    if (viewModel.needAuth.value) {
        currentPage = null
        navigateUp = null
        viewModel.needAuth.value = false
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }
    }

    LazyScores(
        scores,
        onRefresh = { viewModel.fetchAndAddToDb() },
        listState = listState,
        item = {
            Column(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.user.value != null) {
                    UserCard(viewModel.user.value!!, true)
                } else {
                    YouSpinMeRightRoundBabyRightRound()
                }
            }

        },
        isRefreshing = isRefreshing
    )
    if (scores.isEmpty()) {
        if (InternetManager().hasInternetStatus()) {
            if (!viewModel.isLoaded.value)
                YouSpinMeRightRoundBabyRightRound(
                    "Getting best scores... ${viewModel.nowPage.intValue}/${viewModel.pageCount.intValue}",
                    progress = (viewModel.nowPage.intValue.toFloat() / viewModel.pageCount.intValue)
                )
            else
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No Scores")
                }
        } else
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No information.\nPlease reopen app when will be internet")
            }
    }
}