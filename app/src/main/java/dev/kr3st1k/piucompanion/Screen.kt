package dev.kr3st1k.piucompanion

sealed class Screen(val route: String)
{
    object MainScreen : Screen("main_screen")
    object LoginWebViewScreen : Screen("login_web_view_screen")
    object MainAccountScreen : Screen("main_account_screen")
}
