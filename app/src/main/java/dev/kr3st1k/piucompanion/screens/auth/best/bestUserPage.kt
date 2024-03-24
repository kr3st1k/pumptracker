package dev.kr3st1k.piucompanion.screens.auth.best

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.kr3st1k.piucompanion.objects.BestUserScore
import dev.kr3st1k.piucompanion.screens.Screen
import dev.kr3st1k.piucompanion.screens.components.MyAlertDialog
import dev.kr3st1k.piucompanion.screens.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.screens.components.home.scores.best.LazyBestScore

@SuppressLint("MutableCollectionMutableState", "CoroutineCreationDuringComposition")
@Composable
fun BestUserPage(
    navControllerGlobal: NavController,
    lifecycleOwner: LifecycleOwner,
)
{
    val viewModel = viewModel<BestUserViewModel>(
        factory = BestUserViewModelFactory(
            LocalContext.current
        )
    )
    var checkLogin by remember {
        mutableStateOf(false)
    }
    val checkLoginObserver = Observer<Boolean> {
        checkLogin = it
    }

    var checkingLogin by remember {
        mutableStateOf(true)
    }
    val checkingLoginObserver = Observer<Boolean> {
        checkingLogin = it
    }

    var scores by remember { mutableStateOf<Pair<MutableList<BestUserScore>, Boolean>?>(null) }

    val scoresObserver = Observer<Pair<MutableList<BestUserScore>, Boolean>> { newScores ->
        scores = newScores
    }

    viewModel.checkingLogin.observe(lifecycleOwner, checkingLoginObserver)
    viewModel.checkLogin.observe(lifecycleOwner, checkLoginObserver)
    viewModel.scores.observe(lifecycleOwner, scoresObserver)

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        if (checkingLogin) {
            YouSpinMeRightRoundBabyRightRound("Check if you logged in...")
        } else {
            if (checkLogin) {
                if (scores?.first?.isNotEmpty() == true) {
                    LazyBestScore(scores!!)
                }
                else
                {
                    YouSpinMeRightRoundBabyRightRound("Getting best scores...")
                }
            } else {
                MyAlertDialog(
                    showDialog = true,
                    title = "Login failed!",
                    content = "You need to authorize",
                    onDismiss = {
                        navControllerGlobal.navigate(Screen.LoginWebViewScreen.route) {
                            popUpTo(Screen.HomeScreen.route) { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}