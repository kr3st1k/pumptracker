package dev.kr3st1k.piucompanion.screens

sealed class Screen(val route: String)
{
    object HomeScreen : Screen("home_screen")
    object WelcomeScreen : Screen("welcome_screen")
    object LoginWebViewScreen : Screen("login_web_view_screen")

    object NewsPage : Screen("news_page")
}
