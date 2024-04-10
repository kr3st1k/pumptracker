package dev.kr3st1k.piucompanion.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.kr3st1k.piucompanion.ui.components.Button
import dev.kr3st1k.piucompanion.ui.components.MyAlertDialog

@Composable
fun SettingsPage(
    navController: NavController,
//    viewModel: HistoryViewModel
) {
    val showLogoutDialogue = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            MyAlertDialog(
                showDialog = showLogoutDialogue.value,
                title = "Exit from account?",
                content = "Are you sure about that?",
                onDismiss = {
                    showLogoutDialogue.value = false
                },
                onConfirm = {
                    showLogoutDialogue.value = false
                }
            )
            Button(
                icon = Icons.Default.Info,
                title = "About",
                summary = "Who made this? Where can i complain",
                onClick = {}
            )
            Spacer(modifier = Modifier.size(14.dp))
            Button(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = "Log Out",
                summary = "Exit from the account",
                onClick = {
                    showLogoutDialogue.value = true
                }
            )

        }
    }
}