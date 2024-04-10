package dev.kr3st1k.piucompanion.ui.screens

data class TopLevelDestination(
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val iconText: String,
    val summary: String? = null,
)
