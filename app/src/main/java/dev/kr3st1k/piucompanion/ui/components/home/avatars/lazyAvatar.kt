package dev.kr3st1k.piucompanion.ui.components.home.avatars

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.AvatarItem
import dev.kr3st1k.piucompanion.ui.components.DialogWithImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyAvatar(
    avatars: List<AvatarItem>,
    onRefresh: () -> Unit,
    item: @Composable (() -> Unit)? = null,
    onUpdate: () -> Unit,
    userMoney: String
) {
    var isRefreshing by remember { mutableStateOf(false) }

    var isBought by remember {
        mutableStateOf(false)
    }

    var isUpdating by remember {
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

    val scaleFraction = {
        if (isRefreshing) 0f
        else LinearOutSlowInEasing.transform(state.distanceFraction).coerceIn(0f, 1f)
    }
    val stateGrid = rememberLazyGridState()
    if (isUpdating) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        if (isBought) {
            DialogWithImage(
                onDismissRequest = { isBought = !isBought },
                onConfirmation = {
                    scope.launch {
                        NetworkRepositoryImpl.setAvatar(avatarValue)
                        onUpdate()
                        isUpdating = true
                    }.invokeOnCompletion {
                        isBought = !isBought
                        isUpdating = false
                    }
                },
                uri = avatarImageUri,
                imageDescription = "Avatar is ready!\nEquip an avatar?"
            )
        }
        if (isBuying) {
            DialogWithImage(
                onDismissRequest = { isBuying = !isBuying },
                onConfirmation = {
                    scope.launch {
                        NetworkRepositoryImpl.buyAvatar(avatarValue)
                        isUpdating = true
                        onUpdate()
                    }.invokeOnCompletion {
                        onUpdate()
                        isUpdating = false
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = stateGrid,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.background)
                .pullToRefresh(
                    state = state,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        scope.launch {
                            state.animateToHidden()
                            isRefreshing = true
                            onRefresh()
                            isRefreshing = false
                        }
                    }
                )
        ) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                if (item != null) {
                    Box(
                        modifier = Modifier.padding(bottom = 8.dp)
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
                                NetworkRepositoryImpl.setAvatar(data.value)
                                isUpdating = true
                                onUpdate()
                            }.invokeOnCompletion {
                                isUpdating = false
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


        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    scaleX = scaleFraction()
                    scaleY = scaleFraction()
                }
        ) {
            if (avatars.isNotEmpty())
                PullToRefreshDefaults.Indicator(state = state, isRefreshing = isRefreshing)
        }
    }
}