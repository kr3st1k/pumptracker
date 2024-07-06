package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.kr3st1k.piucompanion.core.viewmodels.AvatarShopViewModel
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.avatars.LazyAvatar
import dev.kr3st1k.piucompanion.ui.components.home.users.UserCard
import dev.kr3st1k.piucompanion.ui.pages.Screen

@Composable
fun AvatarShopScreen(
    viewModel: AvatarShopViewModel,
    navController: NavHostController,
    listState: LazyGridState
) {
    val avatars by viewModel.avatars.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    if (avatars == null && viewModel.user.value == null) {
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }
    } else {
        LazyAvatar(
            avatars!!,
            onRefresh = { viewModel.loadAvatars() },
            onSetAvatar = { value -> viewModel.setAvatar(value) },
            onBuyAvatar = { value -> viewModel.buyAvatar(value) },
            isRefreshing = isRefreshing,
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
                        UserCard(viewModel.user.value!!, true, showMoney = true)
                    } else {
                        YouSpinMeRightRoundBabyRightRound()
                    }
                }
                Spacer(modifier = Modifier.size(4.dp))

            },
            userMoney = viewModel.user.value?.coinValue ?: "0",
            listState = listState
        )
        if (avatars?.isEmpty() == true) {
            YouSpinMeRightRoundBabyRightRound("Getting avatars...")
        }
    }

}

