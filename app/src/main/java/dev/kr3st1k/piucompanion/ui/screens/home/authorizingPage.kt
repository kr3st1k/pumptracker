package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.helpers.Crypto
import dev.kr3st1k.piucompanion.core.modules.LoginManager
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.screens.Screen
import kotlinx.coroutines.launch

@Composable
fun AuthLoadingPage(viewModel: AuthViewModel, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize())
    {
        YouSpinMeRightRoundBabyRightRound("Authorizing...")
    }
    if (!viewModel.isLoading.value)
        if (viewModel.isFailed.value)
            navController.navigate(Screen.LoginPage.route) {
                popUpTo(navController.graph.id)
                {
                    inclusive = true
                }
            }
        else
            navController.navigate(Screen.HistoryPage.route) {
                popUpTo(navController.graph.id)
                {
                    inclusive = true
                }
            }
}

class AuthViewModel : ViewModel() {
    val isFailed = mutableStateOf(false)
    val isLoading = mutableStateOf(true)

    init {
        startAuth()
    }

    private fun startAuth() {
        val loginData = LoginManager().getLoginData()
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
    }
}