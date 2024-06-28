package dev.kr3st1k.piucompanion.ui.pages

import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val showTitle: Boolean = true,
    val summary: String? = null,
)
