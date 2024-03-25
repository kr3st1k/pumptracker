package dev.kr3st1k.piucompanion.screens.home.scores.best

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
import dev.kr3st1k.piucompanion.components.MyAlertDialog
import dev.kr3st1k.piucompanion.components.YouSpinMeRightRoundBabyRightRound
import dev.kr3st1k.piucompanion.components.home.scores.best.DropdownMenuBestScores
import dev.kr3st1k.piucompanion.components.home.scores.best.LazyBestScore
import dev.kr3st1k.piucompanion.objects.BestUserScore
import dev.kr3st1k.piucompanion.screens.Screen

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

    var isRecent by remember {
        mutableStateOf(false)
    }
    val isRecentObserver = Observer<Boolean> {
        isRecent = it
    }

    var selectedOption by remember {
        mutableStateOf(Pair("All", ""))
    }
    val selectedOptionObserver = Observer<Pair<String, String>> {
        selectedOption = it
    }

    var scores by remember { mutableStateOf<List<BestUserScore>?>(null) }

    val scoresObserver = Observer<List<BestUserScore>> { newScores ->
        scores = newScores
    }

    viewModel.checkingLogin.observe(lifecycleOwner, checkingLoginObserver)
    viewModel.checkLogin.observe(lifecycleOwner, checkLoginObserver)
    viewModel.scores.observe(lifecycleOwner, scoresObserver)
    viewModel.isRecent.observe(lifecycleOwner, isRecentObserver)
    viewModel.selectedOption.observe(lifecycleOwner, selectedOptionObserver)

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        DropdownMenuBestScores(
            viewModel.options,
            selectedOption,
            onUpdate = { viewModel.refreshScores(it) })
        if (checkingLogin) {
            YouSpinMeRightRoundBabyRightRound("Check if you logged in...")
        } else {
            if (checkLogin) {
                if (scores?.isNotEmpty() == true) {
                    LazyBestScore(
                        scores!!,
                        onRefresh = { viewModel.loadScores() },
                        onLoadNext = { viewModel.addScores() },
                        isRecent = isRecent
                    )
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