package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.kr3st1k.piucompanion.core.viewmodels.TitleShopViewModel
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.titles.LazyTitle
import dev.kr3st1k.piucompanion.ui.components.home.users.UserCard
import dev.kr3st1k.piucompanion.ui.pages.Screen

@Composable
fun TitleShopScreen(
    viewModel: TitleShopViewModel,
    navController: NavHostController,
    listState: LazyGridState
) {
    val titles by viewModel.titles.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    if (titles == null && viewModel.user.value == null) {
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }
    } else {
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

}