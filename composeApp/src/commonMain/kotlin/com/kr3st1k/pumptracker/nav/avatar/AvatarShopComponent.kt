package com.kr3st1k.pumptracker.nav.avatar

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.core.network.data.avatar.AvatarItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class AvatarShopComponent(
    val navigateToLogin: () -> Unit,
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    val avatars = MutableStateFlow<List<AvatarItem>?>(mutableListOf())
    val user: MutableState<User?> = mutableStateOf(null)
    val isRefreshing = MutableStateFlow(false)

    init {
        lifecycle.doOnResume {
            loadAvatars()
        }
    }

    fun loadAvatars() {
        viewModelScope.launch {
            isRefreshing.value = true
            val data = NetworkRepositoryImpl.getAvatarShopInfo()
            if (data == null) {
                navigateToLogin()
            } else {
                avatars.value = data.items
                user.value = data.user
            }
            isRefreshing.value = false
        }
    }

    suspend fun buyAvatar(value: String) {
        NetworkRepositoryImpl.buyAvatar(value)
        loadAvatars()
    }

    suspend fun setAvatar(value: String) {
        isRefreshing.value = true
        NetworkRepositoryImpl.setAvatar(value)
        loadAvatars()
    }
}