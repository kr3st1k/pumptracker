package dev.kr3st1k.piucompanion.screens.home.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.components.MyAlertDialog
import dev.kr3st1k.piucompanion.components.Utils
import dev.kr3st1k.piucompanion.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.components.home.users.UserCard
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.objects.User
import dev.kr3st1k.piucompanion.screens.Screen

@Composable
fun UserScreen(
    navController: NavController,
    navControllerGlobal: NavController,
    lifecycleOwner: LifecycleOwner,
) {

    val context = LocalContext.current
    val pref = PreferencesManager(context)
    val viewModel = viewModel<UserViewModel>(factory = UserViewModelFactory(pref))

    val checkLogin =
        Utils.rememberLiveData(viewModel.checkLogin, lifecycleOwner, initialValue = false)
    val checkingLogin =
        Utils.rememberLiveData(viewModel.checkingLogin, lifecycleOwner, initialValue = true)
    val user =
        Utils.rememberLiveData(viewModel.user, lifecycleOwner, initialValue = User())

    if (checkingLogin.value) {
        YouSpinMeRightRoundBabyRightRound("Checking if you're logged in...")
    } else {
        if (checkLogin.value) {
            if (user.value.trueUser) {
                Column(modifier = Modifier.fillMaxSize()) {
                    UserCard(user.value)
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
        } else {
            MyAlertDialog(
                showDialog = true,
                title = "Login failed!",
                content = "You need to authorize",
                onDismiss = {
                    navControllerGlobal.navigate(Screen.LoginWebViewScreen.route)
                }
            )
        }
    }
}