package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.core.network.data.title.TitleItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TitleShopViewModel : ViewModel() {
    val titles = MutableStateFlow<List<TitleItem>?>(mutableListOf())
    val user: MutableState<User?> = mutableStateOf(null)
    val isRefreshing = MutableStateFlow(false)

    init {
        loadTitles()
    }

    fun loadTitles() {
        viewModelScope.launch {
            isRefreshing.value = true
            val data = NetworkRepositoryImpl.getTitleShopInfo()
            if (data == null) {
                titles.value = null
            } else {
                titles.value = data.titles
                user.value = data.user
            }
            isRefreshing.value = false
        }
    }

    suspend fun setAvatar(value: String) {
        isRefreshing.value = true
        NetworkRepositoryImpl.setTitle(value)
        loadTitles()
    }

}