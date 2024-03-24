package dev.kr3st1k.piucompanion.screens.auth.history

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.helpers.RequestHandler
import dev.kr3st1k.piucompanion.objects.LatestScore
import dev.kr3st1k.piucompanion.screens.Screen
import dev.kr3st1k.piucompanion.screens.components.MyAlertDialog
import dev.kr3st1k.piucompanion.screens.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.screens.components.home.scores.LazyLatestScoreMini
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@Composable
fun ActivityPage(navControllerGlobal: NavController)
{
    val scope = rememberCoroutineScope()
    val pref = PreferencesManager(LocalContext.current)
    val checkingLogin = remember {
        mutableStateOf(true)
    }
    val checkLogin = remember { mutableStateOf(false) };
    val scores = remember { mutableStateOf<MutableList<LatestScore>>(mutableListOf()) }
    scope.launch {
        checkLogin.value = RequestHandler.checkIfLoginSuccess(pref.getData("cookies", ""), pref.getData("ua", ""))
        checkingLogin.value = false
    }
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        if (checkingLogin.value) {
            YouSpinMeRightRoundBabyRightRound("Check if you logged in...")
        } else {
            if (checkLogin.value) {
                scope.launch {
                    scores.value = RequestHandler.getLatestScores(
                        pref.getData("cookies", ""),
                        pref.getData("ua", ""),
                        50
                    )
                }
                if (scores.value.isNotEmpty()) {
                    LazyLatestScoreMini(scores.value, onRefresh = {
                        scope.launch {
                            scores.value = mutableListOf()
                            scores.value = RequestHandler.getLatestScores(
                                pref.getData("cookies", ""),
                                pref.getData("ua", ""),
                                50
                            )
                        }
                    })
                } else {
                    YouSpinMeRightRoundBabyRightRound("Getting latest scores...")
                }
            } else {
                MyAlertDialog(
                    showDialog = !checkLogin.value,
                    title = "Login failed!",
                    content = "You need to authorize",
                    onDismiss = {
                        navControllerGlobal.navigate(Screen.LoginWebViewScreen.route)
                    }
                )
            }


        }
    }
}