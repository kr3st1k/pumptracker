package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    if (user?.trueUser == true) {
                Column(modifier = Modifier.fillMaxSize()) {
                    UserCard(user!!)
                    Spacer(modifier = Modifier.size(14.dp))
                    Box(
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
                    }

//                    Button(
//                        icon = Icons.Default.Info,
//                        title = "Titles",
//                        summary = "wow. button",
//                        onClick = {}
//                    )
//                    Button(
//                        icon = Icons.Default.Info,
//                        title = "Avatar Shop",
//                        summary = "wow. button",
//                        onClick = {}
//                    )
//                    Button(
//                        icon = Icons.Default.Settings,
//                        title = "Settings",
//                        summary = "wow. button",
//                        onClick = {}
//                    )
//                    Button(
//                        icon = Icons.Default.Info,
//                        title = "Logout from account",
//                        summary = "wow. button",
//                        onClick = {
//                            viewModel.logout(pref)
//                            navControllerGlobal.navigate(Screen.NewsPage.route)
//                        }
//                    )
//                    Button(
//                        icon = Icons.Default.Info,
//                        title = "About",
//                        summary = "wow. button",
//                        onClick = {}
//                    )
                }
            } else {
                YouSpinMeRightRoundBabyRightRound("Getting User Info...")
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