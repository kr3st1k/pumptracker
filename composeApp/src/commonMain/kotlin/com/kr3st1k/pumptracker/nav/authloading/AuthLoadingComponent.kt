package com.kr3st1k.pumptracker.nav.authloading

import com.arkivanov.decompose.ComponentContext
import com.kr3st1k.pumptracker.core.helpers.Crypto
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.di.BgManager
import com.kr3st1k.pumptracker.di.InternetManager
import com.kr3st1k.pumptracker.di.LoginManager
import com.kr3st1k.pumptracker.nav.RootComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent

class AuthLoadingComponent(
    val navigateTo: (RootComponent.TopLevelConfiguration) -> Unit,
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent {
    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)

    init {
        startAuth()
    }

    private fun startAuth() {
        runBlocking {
            BgManager().saveJKS()
        }
        val loginData = LoginManager().getLoginData()
        if (InternetManager().hasInternetStatus()) {
            viewModelScope.launch {
                val r = loginData.first?.let { Crypto.decryptData(it) }
                    ?.let {
                        loginData.second?.let { it1 -> Crypto.decryptData(it1) }?.let { it2 ->
                            NetworkRepositoryImpl.loginToAmPass(it, it2)
                        }
                    }
                if (r == null || r == false) {
                    navigateTo(RootComponent.TopLevelConfiguration.AuthPage)
                    return@launch
                }
                navigateTo(RootComponent.TopLevelConfiguration.HistoryPage)
            }
        } else {
            navigateTo(RootComponent.TopLevelConfiguration.HistoryPage)
        }
    }
}