package com.kr3st1k.pumptracker.ui.pages

import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val showTitle: Boolean = true,
    val summary: String? = null,
    val availableAtOffline: Boolean = false
)
