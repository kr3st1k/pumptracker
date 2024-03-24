package dev.kr3st1k.piucompanion.screens.auth.best

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
import dev.kr3st1k.piucompanion.objects.BestUserScore
import dev.kr3st1k.piucompanion.objects.checkAndSaveNewUpdatedFiles
import dev.kr3st1k.piucompanion.objects.readBgJson
import dev.kr3st1k.piucompanion.screens.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.screens.components.home.scores.LazyBestScoreMini
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState", "CoroutineCreationDuringComposition")
@Composable
fun BestUserPage(navControllerGlobal: NavController)
{
    val scope = rememberCoroutineScope()
    val pref = PreferencesManager(LocalContext.current)
    val context = LocalContext.current
    val checkingLogin = remember {
        mutableStateOf(true)
    }
    val checkLogin = remember { mutableStateOf(false) };
    val scores = remember { mutableStateOf(
        Pair<MutableList<BestUserScore>, Boolean>(mutableListOf(), false)
    ) }
    scope.launch {
        checkAndSaveNewUpdatedFiles(context)
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
                    val bgs = readBgJson(context)
                    scores.value = RequestHandler.getBestUserScores(
                        pref.getData("cookies", ""),
                        pref.getData("ua", ""),
                        bgs = bgs
                    )
                }
                if (scores.value.first.isNotEmpty()) {
                    LazyBestScoreMini(scores.value, onRefresh = {
                        scope.launch {
                            val bgs = readBgJson(context)
                            scores.value.first.clear()
                            scores.value = RequestHandler.getBestUserScores(
                                pref.getData("cookies", ""),
                                pref.getData("ua", ""),
                                bgs = bgs
                            )
                        }
                    })
                }
                else
                {
                    YouSpinMeRightRoundBabyRightRound("Getting best scores...")
                }
            }
        }
    }
}