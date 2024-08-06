package com.kr3st1k.pumptracker.ui.pages.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kr3st1k.pumptracker.core.db.repository.ScoresRepository
import com.kr3st1k.pumptracker.core.network.NetworkRepositoryImpl
import com.kr3st1k.pumptracker.di.DbManager
import com.kr3st1k.pumptracker.di.LoginManager
import com.kr3st1k.pumptracker.ui.components.dialogs.AlertDialogWithTwoButtons
import com.kr3st1k.pumptracker.ui.pages.Screen
import com.kr3st1k.pumptracker.ui.pages.currentPage
import com.kr3st1k.pumptracker.ui.pages.navigateUp
import kotlinx.coroutines.launch
import com.kr3st1k.pumptracker.ui.pages.refreshFunction

@Composable
fun SettingsScreen(
    navController: NavController,
) {
    val showLogoutDialogue = remember {
        mutableStateOf(false)
    }
    
    refreshFunction.value = null

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
                refreshFunction.value = null
                currentPage = null
                navigateUp = null
                navController.navigate(Screen.LoginPage.route) {
                    popUpTo(Screen.SettingsPage.route)
                    {
                        inclusive = true
                    }
                    popUpTo(Screen.UserPage.route)
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
