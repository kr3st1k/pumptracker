package dev.kr3st1k.piucompanion

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.screens.Screen
import dev.kr3st1k.piucompanion.screens.unauth.LoginWebViewScreen
import dev.kr3st1k.piucompanion.screens.unauth.MainScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route)
    {
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(route = Screen.LoginWebViewScreen.route) {
            LoginWebViewScreen(navController = navController)
        }
        composable(route = Screen.MainAccountScreen.route) {
            MainAccountScreen(navController = navController)
        }
    }
}


@Composable
fun MyAlertDialog(
    showDialog: Boolean,
    title: String,
    content: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = {
                Text(text = content)
            },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                    // Действие при нажатии на кнопку "Подтвердить"
                }) {
                    Text(text = "Ща зайду, пажжи")
                }
            }
        )
    }
}

@Composable
fun MainAccountScreen(navController: NavController) {


}


