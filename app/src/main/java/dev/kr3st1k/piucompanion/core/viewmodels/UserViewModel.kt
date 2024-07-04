package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(User())
    val user: StateFlow<User?> = _user

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _user.value =
                NetworkRepositoryImpl.getUserInfo()
        }
    }
}