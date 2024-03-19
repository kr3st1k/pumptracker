package dev.kr3st1k.piucompanion.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.screens.Screen
import dev.kr3st1k.piucompanion.screens.components.MyAlertDialog
import dev.kr3st1k.piucompanion.screens.components.YouSpinMeRightRoundBabyRightRound
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UserScreen(navController: NavController, navControllerGlobal: NavController)
{
    val scope = rememberCoroutineScope()
    val pref = PreferencesManager(LocalContext.current)
    val checkingLogin = remember {
        mutableStateOf(true)
    }
    val checkLogin = remember { mutableStateOf(false) };
    scope.launch {
        checkLogin.value = RequestHandler.checkIfLoginSuccess(pref.getData("cookies", ""), pref.getData("ua", ""))
        checkingLogin.value = false
    }
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        if (checkingLogin.value) {
            YouSpinMeRightRoundBabyRightRound()
        } else {
            if (checkLogin.value) {
                Text(text = "Success!")
            } else {
                MyAlertDialog(
                    showDialog = !checkLogin.value,
                    title = "Авторизуйтесь заново",
                    content = "Вам необходимо зайти заново",
                    onDismiss = {
                        navControllerGlobal.navigate(Screen.LoginWebViewScreen.route)
                    }
                )
            }


        }
    }
}