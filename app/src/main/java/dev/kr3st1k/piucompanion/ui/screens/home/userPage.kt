package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.R
import dev.kr3st1k.piucompanion.core.network.NetworkRepositoryImpl
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.ui.components.Button
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.users.UserCard
import dev.kr3st1k.piucompanion.ui.screens.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    navController: NavController,
    viewModel: UserViewModel,
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    if (user == null)
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }
    Column(modifier = Modifier.fillMaxSize()) {


        Column(modifier = Modifier
            .fillMaxHeight(0.25f)
            .fillMaxWidth()) {
            if (user?.trueUser == true) {
                UserCard(user!!)
            } else {
                YouSpinMeRightRoundBabyRightRound()
            }
        }
        Spacer(modifier = Modifier.size(14.dp))
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        {
            Button(
                icon = ImageVector.vectorResource(R.drawable.baseline_newspaper_24),
                title = "News",
                summary = "Read latest news about Pump It Up",
                onClick = {
                    navController.navigate(Screen.NewsPage.route)
                }
            )
            Spacer(modifier = Modifier.size(12.dp))
            Button(
                icon = ImageVector.vectorResource(R.drawable.baseline_leaderboard_24),
                title = "Leaderboard",
                summary = "Let's see who sniped FEFEMZ's scores",
                onClick = {
                    navController.navigate(Screen.NewsPage.route)
                }
            )
            Spacer(modifier = Modifier.size(12.dp))
            Button(
                icon = ImageVector.vectorResource(R.drawable.baseline_dvr_24),
                title = "Title Changer",
                summary = "Select the most beautiful title available",
                onClick = {
                    navController.navigate(Screen.NewsPage.route)
                }
            )
            Spacer(modifier = Modifier.size(12.dp))
            Button(
                icon = Icons.Filled.AccountCircle,
                title = "Avatar Shop",
                summary = "Wow, nice picture!",
                onClick = {
                    navController.navigate(Screen.NewsPage.route)
                }
            )
            Spacer(modifier = Modifier.size(12.dp))
            Button(
                icon = Icons.Filled.Settings,
                title = "Settings",
                summary = "Touch me, Senpai!",
                onClick = {
                    navController.navigate(Screen.SettingsPage.route)
                }
            )
        }
    }
}

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