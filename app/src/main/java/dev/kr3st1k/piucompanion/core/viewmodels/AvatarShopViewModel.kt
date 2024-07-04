package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.AvatarItem
import dev.kr3st1k.piucompanion.core.network.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AvatarShopViewModel : ViewModel() {
    val avatars = MutableStateFlow<List<AvatarItem>?>(mutableListOf())
    val user: MutableState<User?> = mutableStateOf(null)
    val isRefreshing = MutableStateFlow(false)

    init {
        loadAvatars()
    }

    fun loadAvatars() {
        viewModelScope.launch {
            isRefreshing.value = true
            val data = NetworkRepositoryImpl.getAvatarShopInfo()
            if (data == null) {
                avatars.value = null
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