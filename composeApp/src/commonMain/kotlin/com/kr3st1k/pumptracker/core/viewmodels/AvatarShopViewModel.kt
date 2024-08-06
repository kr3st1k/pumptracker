package com.kr3st1k.pumptracker.core.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.core.network.data.User
import com.kr3st1k.pumptracker.core.network.data.avatar.AvatarItem
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