package dev.kr3st1k.piucompanion.ui.screens

sealed class Screen(val route: String)
{
    object HomeScreen : Screen("home_screen")
    object WelcomeScreen : Screen("welcome_screen")
    object LoginWebViewScreen : Screen("login_web_view_screen")

    object AuthLoadingPage : Screen("auth_loading_page")

    object NewsPage : Screen("news_page")
    object UserPage : Screen("user_page")
    object LoginPage : Screen("login_page")
    object HistoryPage : Screen("history_page")
    object BestUserPage : Screen("best_user_page")
}
