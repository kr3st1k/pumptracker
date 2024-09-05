package com.kr3st1k.pumptracker.nav.avatar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kr3st1k.pumptracker.nav.refreshFunction
import com.kr3st1k.pumptracker.ui.components.home.avatars.LazyAvatar
import com.kr3st1k.pumptracker.ui.components.home.users.UserCard
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpinMeRightRoundBabyRightRound

@Composable
fun AvatarShopComponentImpl(viewModel: AvatarShopComponent, listState: LazyGridState) {
    val avatars by viewModel.avatars.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    refreshFunction.value = { viewModel.loadAvatars() }

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