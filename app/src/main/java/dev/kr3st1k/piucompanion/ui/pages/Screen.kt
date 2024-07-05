package dev.kr3st1k.piucompanion.ui.pages

sealed class Screen(val route: String)
{
    data object AuthLoadingPage : Screen("auth_loading_page")
    data object NewsPage : Screen("news_page")
    data object UserPage : Screen("user_page")
    data object LoginPage : Screen("login_page")
    data object HistoryPage : Screen("history_page")
    data object BestUserPage : Screen("best_user_page")
    data object PumbilityPage : Screen("pumbility_page")
    data object SettingsPage : Screen("settings_page")
    data object AvatarShopPage : Screen("avatar_page")
    data object TitleShopPage : Screen("title_page")
}
