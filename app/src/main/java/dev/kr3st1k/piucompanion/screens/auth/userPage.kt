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
import dev.kr3st1k.piucompanion.objects.LatestScore
import dev.kr3st1k.piucompanion.objects.NewsThumbnailObject
import dev.kr3st1k.piucompanion.objects.User
import dev.kr3st1k.piucompanion.screens.Screen
import dev.kr3st1k.piucompanion.screens.components.MyAlertDialog
import dev.kr3st1k.piucompanion.screens.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.screens.components.home.users.LazyLatestScoreMini
import dev.kr3st1k.piucompanion.screens.components.home.users.UserCard
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun UserScreen(navController: NavController, navControllerGlobal: NavController)
{
    val scope = rememberCoroutineScope()
    val pref = PreferencesManager(LocalContext.current)
    val checkingLogin = remember {
        mutableStateOf(true)
    }
    val checkLogin = remember { mutableStateOf(false) };
    val user = remember {
        mutableStateOf(User())
    }
    val scores = remember { mutableStateOf<MutableList<LatestScore>>(mutableListOf()) }
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
                scope.launch {
                    user.value = RequestHandler.getUserInfo(pref.getData("cookies", ""), pref.getData("ua", ""))
                }
                if (user.value.trueUser)
                {
                    UserCard(user.value)
                    scope.launch {
                        scores.value = RequestHandler.getLatestScores(pref.getData("cookies", ""), pref.getData("ua", ""), 5)
                    }
                    if (scores.value.isNotEmpty())
                    {
                        LazyLatestScoreMini(scores.value)
                    }
                    else
                    {
                        YouSpinMeRightRoundBabyRightRound()
                    }
                }
                else
                {
                    YouSpinMeRightRoundBabyRightRound()
                }
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