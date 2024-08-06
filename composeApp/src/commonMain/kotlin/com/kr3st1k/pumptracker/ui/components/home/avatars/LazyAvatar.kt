package com.kr3st1k.pumptracker.ui.components.home.avatars

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kr3st1k.pumptracker.core.network.data.avatar.AvatarItem
import com.kr3st1k.pumptracker.getPlatform
import com.kr3st1k.pumptracker.ScrollBarForDesktop
import com.kr3st1k.pumptracker.ui.components.dialogs.AlertDialogWithImage
import kotlinx.coroutines.launch
import com.kr3st1k.pumptracker.ui.components.spinners.YouSpeenMeRightRoundBabyRightRoundTop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyAvatar(
    avatars: List<AvatarItem>,
    onRefresh: () -> Unit,
    item: @Composable() (() -> Unit)? = null,
    userMoney: String,
    listState: LazyGridState,
    isRefreshing: Boolean,
    onSetAvatar: suspend (value: String) -> Unit,
    onBuyAvatar: suspend (value: String) -> Unit
) {
    var isBought by remember {
        mutableStateOf(false)
    }

    var isBuying by remember {
        mutableStateOf(false)
    }

    var avatarValue by remember {
        mutableStateOf("null")
    }

    var avatarPrice by remember {
        mutableStateOf("null")
    }

    var avatarImageUri by remember {
        mutableStateOf("null")
    }

    val scope = rememberCoroutineScope()
    val state = rememberPullToRefreshState()

    if (isBought) {
        AlertDialogWithImage(
            onDismissRequest = { isBought = !isBought },
            onConfirmation = {
                scope.launch {
                    onSetAvatar(avatarValue)
                }.invokeOnCompletion {
                    isBought = !isBought
                }
            },
            uri = avatarImageUri,
            imageDescription = "Avatar is ready!\nEquip an avatar?"
        )
    }
    if (isBuying) {
        AlertDialogWithImage(
            onDismissRequest = { isBuying = !isBuying },
            onConfirmation = {
                scope.launch {
                    onBuyAvatar(avatarValue)
                }.invokeOnCompletion {
                    isBuying = false
                    isBought = true
                }
            },
            uri = avatarImageUri,
            imageDescription = "Will you buy this avatar?\nPrice of Avatar: $$avatarPrice\nYour balance will be: $${
                userMoney.replace(
                    ",",
                    ""
                ).toInt() - avatarPrice.toInt()
            }"
        )
    }
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.background)
                .pullToRefresh(
                    enabled = getPlatform().type != "Desktop",
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                )
        )
        {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (item != null) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        item()
                    }
                }
            }
            items(avatars.filter { it.isBought }) { data ->
                AvatarCard(
                    data,
                    action = {
                        if (!data.isSelected)
                            scope.launch {
                                onSetAvatar(data.value)
                            }

                    }
                )
            }
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (avatars.isNotEmpty())
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 8.dp),
                        thickness = 2.dp,
                        color = Color(0xFF222933)
                    )
            }
            items(avatars.filter { !it.isBought }) { data ->
                AvatarCard(
                    data,
                    action = {
                        avatarValue = data.value
                        avatarPrice = data.price
                        avatarImageUri = data.avatarUrl
                        isBuying = true
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