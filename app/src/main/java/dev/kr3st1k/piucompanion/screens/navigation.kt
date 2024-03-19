package dev.kr3st1k.piucompanion.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.kr3st1k.piucompanion.helpers.PreferencesManager
import dev.kr3st1k.piucompanion.screens.auth.HomeScreen
import dev.kr3st1k.piucompanion.screens.unauth.LoginWebViewScreen
import dev.kr3st1k.piucompanion.screens.unauth.OnboardScreen

@Composable
fun Navigation(context: Context) {
    val navController = rememberNavController()
    val pref = PreferencesManager(context)

    var startDist = Screen.WelcomeScreen.route;

    if (pref.getData("first_run", "true") == "false")
    {
        //TODO Login check with db
        startDist = Screen.HomeScreen.route
    }

    NavHost(navController = navController, startDestination = startDist)
    {
        composable(route = Screen.WelcomeScreen.route) {
            OnboardScreen(navController = navController)
        }
        composable(route = Screen.LoginWebViewScreen.route) {
            LoginWebViewScreen(navController = navController)
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(Screen.NewsPage.route)
        }
    }
}


