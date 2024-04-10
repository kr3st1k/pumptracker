package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.core.helpers.Utils
import dev.kr3st1k.piucompanion.core.network.RequestHandler
import dev.kr3st1k.piucompanion.core.network.data.User
import dev.kr3st1k.piucompanion.ui.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.ui.components.home.users.UserCard
import dev.kr3st1k.piucompanion.ui.screens.Screen
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
) {

    val viewModel = viewModel<UserViewModel>()

    val user =
        Utils.rememberLiveData(viewModel.user, lifecycleOwner, initialValue = User())
    if (user.value == null)
        navController.navigate(Screen.AuthLoadingPage.route) {
            popUpTo(navController.graph.id)
            {
                inclusive = true
            }
        }
    if (user.value?.trueUser == true) {
                Column(modifier = Modifier.fillMaxSize()) {
                    UserCard(user.value!!)
//                    Button(
//                        icon = Icons.Default.Info,
//                        title = "Placeholder",
//                        summary = "Placeholder",
//                        onClick = {}
//                    )
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

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _user.value =
                RequestHandler.getUserInfo()
        }
    }
}