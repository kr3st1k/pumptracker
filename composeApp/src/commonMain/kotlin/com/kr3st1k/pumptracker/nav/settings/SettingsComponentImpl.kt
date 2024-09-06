package com.kr3st1k.pumptracker.nav.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.LoginManager
import com.kr3st1k.pumptracker.ui.components.dialogs.AlertDialogWithTwoButtons
import kotlinx.coroutines.launch

@Composable
fun SettingsComponentImpl(viewModel: SettingsComponent) {
    val showLogoutDialogue = remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val scoresRepository = ScoresRepository(DbManager().getScoreDao())

    var isDarkTheme by remember { mutableStateOf(LoginManager().getIsDarkTheme()) }
    var isDynamicColor by remember { mutableStateOf(LoginManager().getIsDynamicColor()) }
    var isSystemDefault by remember { mutableStateOf(LoginManager().getIsSystemDefault()) }

    AlertDialogWithTwoButtons(
        showDialog = showLogoutDialogue.value,
        title = "Exit from account?",
        content = "Are you sure about that?",
        onDismiss = {
            showLogoutDialogue.value = false
        },
        onConfirm = {
            scope.launch {
                NetworkRepositoryImpl.logout()
                LoginManager().removeLoginData()
                scoresRepository.deleteScores()
                showLogoutDialogue.value = false


                viewModel.navigateToStart()
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
                    Text(
                        text = "Theme Preferences",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                }
            )
            Column {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                ListItem(
                    headlineContent = {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = "Use Dynamic Color"
                        )
                    },
                    trailingContent = {
                        Switch(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            checked = isDynamicColor,
                            onCheckedChange = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            LoginManager().saveIsDynamicColor(!isDynamicColor)
                            isDynamicColor = !isDynamicColor
                        }
                )
                ListItem(
                    leadingContent = {
                        RadioButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            selected = isSystemDefault,
                            onClick = null
                        )
                    },
                    headlineContent = {
                        Text(text = "System Default")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            LoginManager().saveIsSystemDefault(true)
                            isSystemDefault = true
                        }
                )
                ListItem(
                    leadingContent = {
                        RadioButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            selected = !isSystemDefault && !isDarkTheme,
                            onClick = null
                        )
                    },
                    headlineContent = {
                        Text(text = "Light")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            LoginManager().saveIsDarkTheme(false)
                            isDarkTheme = false
                            isSystemDefault = false
                        }
                )
                ListItem(
                    leadingContent = {
                        RadioButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            selected = !isSystemDefault && isDarkTheme,
                            onClick = null
                        )
                    },
                    headlineContent = {
                        Text(text = "Dark")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            LoginManager().saveIsDarkTheme(true)
                            isDarkTheme = true
                            isSystemDefault = false
                        }
                )
            }
        }
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