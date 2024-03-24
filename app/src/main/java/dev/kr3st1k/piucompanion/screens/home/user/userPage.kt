package dev.kr3st1k.piucompanion.screens.home.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.components.Button
import dev.kr3st1k.piucompanion.components.MyAlertDialog
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
    val viewModelFactory = UserViewModelFactory(pref)
    val viewModel = viewModel<UserViewModel>(factory = viewModelFactory)

    var checkingLogin by remember {
        mutableStateOf(true)
    }
    val checkingLoginObserver = Observer<Boolean> {
        checkingLogin = it
    }
    var checkLogin by remember {
        mutableStateOf(false)
    }
    val checkLoginObserver = Observer<Boolean> {
        checkLogin = it
    }
    var user by remember {
        mutableStateOf(User())
    }
    val userObserver = Observer<User> {
        user = it
    }
    viewModel.checkingLogin.observe(lifecycleOwner, checkingLoginObserver)
    viewModel.checkLogin.observe(lifecycleOwner, checkLoginObserver)
    viewModel.user.observe(lifecycleOwner, userObserver)

    if (checkingLogin) {
        YouSpinMeRightRoundBabyRightRound("Checking if you're logged in...")
    } else {
        if (checkLogin) {
            if (user.trueUser) {
                Column(modifier = Modifier.fillMaxSize()) {
                    UserCard(user)
                    Button(
                        icon = Icons.Default.Info,
                        title = "Здесь фиг знает че сунуть",
                        summary = "Лучшие скоры будут снизу, мб всякие топы с сайта",
                        onClick = {}
                    )
                    Button(
                        icon = Icons.Default.Info,
                        title = "Titles",
                        summary = "wow. button",
                        onClick = {}
                    )
                    Button(
                        icon = Icons.Default.Info,
                        title = "Avatar Shop",
                        summary = "wow. button",
                        onClick = {}
                    )
                    Button(
                        icon = Icons.Default.Settings,
                        title = "Settings",
                        summary = "wow. button",
                        onClick = {}
                    )
                    Button(
                        icon = Icons.Default.Info,
                        title = "Logout from account",
                        summary = "wow. button",
                        onClick = {
                            viewModel.logout(pref)
                            navControllerGlobal.navigate(Screen.LoginWebViewScreen.route)
                        }
                    )
                    Button(
                        icon = Icons.Default.Info,
                        title = "About",
                        summary = "wow. button",
                        onClick = {}
                    )
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