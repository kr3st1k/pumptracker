package dev.kr3st1k.piucompanion.core.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kr3st1k.piucompanion.MainActivity
import dev.kr3st1k.piucompanion.core.helpers.Crypto
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.di.InternetManager
import dev.kr3st1k.piucompanion.di.LoginManager
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    val isFailed = mutableStateOf(false)
    val isLoading = mutableStateOf(true)

    init {
        startAuth()
    }

    private fun startAuth() {
        val loginData = LoginManager().getLoginData()
        if (InternetManager().hasInternetStatus()) {
            viewModelScope.launch {
                val r = loginData.first?.let { Crypto.decryptData(it) }
                    ?.let {
                        loginData.second?.let { it1 -> Crypto.decryptData(it1) }?.let { it2 ->
                            NetworkRepositoryImpl.loginToAmPass(it, it2)
                        }
                    }
                if (r == null || r == false)
                    isFailed.value = true
                isLoading.value = false
            }
        } else {
            MainActivity.isOffline = true
            isLoading.value = false
        }
    }
}