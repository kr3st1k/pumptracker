package dev.kr3st1k.piucompanion.ui.screens

sealed class Screen(val route: String)
{
    object AuthLoadingPage : Screen("auth_loading_page")
    object NewsPage : Screen("news_page")
    object UserPage : Screen("user_page")
    object LoginPage : Screen("login_page")
    object HistoryPage : Screen("history_page")
    object BestUserPage : Screen("best_user_page")

    object SettingsPage : Screen("settings_page")
}
