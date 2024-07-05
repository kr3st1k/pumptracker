package dev.kr3st1k.piucompanion.ui.pages.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.di.DbManager
import dev.kr3st1k.piucompanion.di.LoginManager
import dev.kr3st1k.piucompanion.ui.components.AlertDialogWithTwoButton
import dev.kr3st1k.piucompanion.ui.pages.Screen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SettingsPage(
    navController: NavController,
) {
    val showLogoutDialogue = remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    AlertDialogWithTwoButton(
        showDialog = showLogoutDialogue.value,
        title = "Exit from account?",
        content = "Are you sure about that?",
        onDismiss = {
            showLogoutDialogue.value = false
        },
        onConfirm = {
            scope.launch {
                LoginManager().removeLoginData()
                GlobalScope.async {
                    DbManager().getScoreDao().deleteAllBest()
                    DbManager().getScoreDao().deleteAllLatest()
                }.await()
                showLogoutDialogue.value = false
                navController.navigate(Screen.LoginPage.route) {
                    popUpTo(navController.graph.id)
                    {
                        inclusive = true
                    }
                }
            }
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {

        item {
            ListItem(
                headlineContent = {
                    Text(text = "Log Out")
                },
                supportingContent = {
                    Text(text = "Exit from the account")
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Log Out"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showLogoutDialogue.value = true
                    }
            )
        }

        item {
            ListItem(
                headlineContent = {
                    Text(text = "About")
                },
                supportingContent = {
                    Text(text = "Who made this? Where can i complain")
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        print("TODO BRO")
                    }
            )
        }
    }
}
